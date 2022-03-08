package com.mycompany.myapp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.config.EmailUtil;
import com.mycompany.myapp.domain.Abono;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.AbonoRepository;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.CitaTattoService;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
import com.mycompany.myapp.service.dto.MensajeValidacionCitaDTO;
import com.mycompany.myapp.service.mapper.CitaTattoMapper;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CitaTatto}.
 */
@Service
@Transactional
public class CitaTattoServiceImpl implements CitaTattoService {

    private final Logger log = LoggerFactory.getLogger(CitaTattoServiceImpl.class);

    private final CitaTattoRepository citaTattoRepository;

    private final AbonoRepository abonoRepository;

    private final CitaTattoMapper citaTattoMapper;

    public static String MENSAJE_CITA_INVALIDA;

    @PersistenceContext
    private EntityManager entityManager;

    public CitaTattoServiceImpl(AbonoRepository abonoRepository, CitaTattoRepository citaTattoRepository, CitaTattoMapper citaTattoMapper) {
        this.citaTattoRepository = citaTattoRepository;
        this.citaTattoMapper = citaTattoMapper;
        this.abonoRepository = abonoRepository;
    }

    @Override
    public CitaTattoDTO save(CitaTattoDTO citaTattoDTO) {
        log.debug("Request to save CitaTatto : {}", citaTattoDTO);
        CitaTatto citaTatto = citaTattoMapper.toEntity(citaTattoDTO);
        citaTatto.setFechaCreacion(Instant.now());

        if (citaTatto.getDeuda().equals(BigDecimal.ZERO)) {
            citaTatto.estado("Pagada");
        }

        // SE QUITAN LOS ESPACIOS EN BLANCO A TODAS LAS HORAS
        String hora = citaTatto.getHora();
        String formatoHora = hora.replaceAll("\\s+", "").strip();
        citaTatto.setHora(formatoHora);

        String horaInicio = citaTatto.getHoraInicio();
        String horaIniFormat = horaInicio.replaceAll("\\s+", "").strip();
        citaTatto.setHoraInicio(horaIniFormat);

        String horaFin = citaTatto.getHoraFin();
        String horaFinFormat = horaFin.replaceAll("\\s+", "");
        citaTatto.setHoraFin(horaFinFormat);

        if (citaTattoDTO.getValorPagado() == null) {
            citaTattoDTO.setValorPagado(BigDecimal.ZERO);
        }

        if (citaTatto.getFechaCita().isBefore(Instant.now())) {
            citaTatto.setEstadoCita("Finalizada");
        } else if (citaTatto.getFechaCita().isAfter(Instant.now())) {
            citaTatto.setEstadoCita("Pendiente");
        } else {
            citaTatto.setEstadoCita("Pendiente");
        }

        citaTatto = citaTattoRepository.save(citaTatto);
        if (citaTattoDTO.getId() != null) {
            enviarCorreoActualizacion(citaTatto);
        } else {
            enviarCorreoConfirmacion(citaTatto);
        }

        if (citaTattoDTO.getId() == null) {
            Abono abono = null;

            if (citaTatto.getValorPagado() != null) {
                abono = new Abono();
                abono.setIdCita(citaTatto.getId());
                abono.setFechaAbono(citaTatto.getFechaCreacion());
                abono.setTipoCita("Cita Preciado");
                abono.setValorAbono(citaTatto.getValorPagado());

                this.abonoRepository.save(abono);
            }
        }

        return citaTattoMapper.toDto(citaTatto);
    }

    @Override
    public MensajeValidacionCitaDTO validarFechaCita(CitaTattoDTO citaTatto) {
        MensajeValidacionCitaDTO mensajeValidacion = new MensajeValidacionCitaDTO();

        // SE TOMA EL PRIMERO DIGITO DE CADA HORA DE LA CITA QUE SERA VALIDADA CON EL
        // RESTO DE LAS CITAS EXISTENTES.

        String hIni = citaTatto.getHoraInicio();
        String newHIni = hIni.substring(0, 1);
        int horaIn = Integer.parseInt(newHIni);

        String hFn = citaTatto.getHoraFin();
        String newHfn = hFn.substring(0, 1);
        int horaFn = Integer.parseInt(newHfn);

        // VALIDACION CITA DISPONIBLE
        List<CitaTatto> citas = citaTattoRepository.findAll();
        for (CitaTatto cita : citas) {
            // SE TOMA EL PRIMERO DIGITO DE CADA HORA
            String horaIni = cita.getHoraInicio();
            String newHoraIni = horaIni.substring(0, 1);
            int horaIm = Integer.parseInt(newHoraIni);

            String horFin = cita.getHoraFin();
            String newHoraFin = horFin.substring(0, 1);
            int horFn = Integer.parseInt(newHoraFin);

            if (
                cita.getFechaCita().equals(citaTatto.getFechaCita()) &&
                (
                    !(horaIn <= horaIm && horaFn <= horaIm) &&
                    (!(horaIn > horFn && horaFn > horFn)) ||
                    citaTatto.getHora().equalsIgnoreCase(cita.getHora())
                )
            ) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date fech = Date.from(citaTatto.getFechaCita());
                String fecha = format.format(fech);
                mensajeValidacion.setMensaje(
                    "No se puede agendar la cita para la fecha: ( " +
                    fecha +
                    " ) debido " +
                    " ha que ya hay una cita agendada para esta fecha y hora."
                );
            } else {
                mensajeValidacion.setMensaje(null);
            }
        }
        return mensajeValidacion;
    }

    private void enviarCorreoConfirmacion(CitaTatto citaTattoo) {
        if (citaTattoo.getEmailCliente() != null && !citaTattoo.getEmailCliente().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String html = "";
            String mensaje =
                "<html><head><title>Confirmacion Correo</title></head>" +
                "<body style='background-color:black;'><p style='color:aqua;'>Buen dia " +
                citaTattoo.getInfoCliente() +
                ".<br/>" +
                "Su cita a sido agendada con exito para el dia " +
                format.format(Date.from(citaTattoo.getFechaCita())) +
                " a las " +
                citaTattoo.getHora() +
                ".</p>" +
                "<p style='color:aqua;'>Recuerde llegar 10-15 minutos antes para la toma de medidas." +
                "<br/>Muchas Gracias.<br/><br/><br/>Preciado Tattoo.</p></body></html>";

            try {
                log.debug("Request to send mail to client: " + citaTattoo.getEmailCliente());
                EmailUtil.sendArchivoTLS(null, citaTattoo.getEmailCliente().trim(), "Confirmacion Cita Tattoo", mensaje);
                log.debug("Send email to: " + citaTattoo.getEmailCliente());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                log.error("Error enviando notificacion ", e);
                log.error("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void enviarCorreoActualizacion(CitaTatto citaTattoo) {
        if (citaTattoo.getEmailCliente() != null && !citaTattoo.getEmailCliente().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String html = "";
            String mensaje =
                "<html><head><title>Confirmacion Correo</title></head>" +
                "<body style='background-color:black;'><p style='color:aqua;'>Buen dia " +
                citaTattoo.getInfoCliente() +
                ".<br/>" +
                "Su cita a sido Actualizada con exito para el dia " +
                format.format(Date.from(citaTattoo.getFechaCita())) +
                " a las " +
                citaTattoo.getHora() +
                ".</p>" +
                "<p style='color:aqua;'>Recuerde llegar 10-15 minutos antes para la toma de medidas." +
                "<br/>Muchas Gracias.<br/><br/><br/>Preciado Tattoo.</p></body></html>";

            try {
                log.debug("Request to send mail to client: " + citaTattoo.getEmailCliente());
                EmailUtil.sendArchivoTLS(null, citaTattoo.getEmailCliente().trim(), "Confirmacion Cita Tattoo", mensaje);
                log.debug("Send email to: " + citaTattoo.getEmailCliente());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                log.error("Error enviando notificacion ", e);
                log.error("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<CitaTattoDTO> partialUpdate(CitaTattoDTO citaTattoDTO) {
        log.debug("Request to partially update CitaTatto : {}", citaTattoDTO);

        return citaTattoRepository
            .findById(citaTattoDTO.getId())
            .map(existingCitaTatto -> {
                citaTattoMapper.partialUpdate(existingCitaTatto, citaTattoDTO);

                return existingCitaTatto;
            })
            .map(citaTattoRepository::save)
            .map(citaTattoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaTattoDTO> findAll() {
        log.debug("Request to get all CitaTattos");

        Query q = entityManager.createQuery(Constants.ORDENAR_CITAS_PORfECHA);

        List<CitaTattoDTO> citas = q.getResultList();

        return citas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaTattoDTO> citaDia() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fechaHoy = format.format(new Date());

        List<CitaTatto> citaDia = citaTattoRepository.citaDia(fechaHoy);

        return citaTattoMapper.toDto(citaDia);
    }

    @Override
    public List<CitaTattoDTO> citasPorFiltro(CitaTattoDTO citaTatto) {
        log.debug("Request to get all citas per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        sb.append(Constants.CITA_TATTO_BASE);
        if (citaTatto.getInfoCliente() != null && !citaTatto.getInfoCliente().isEmpty()) {
            sb.append(Constants.CITA_TATTO_NOMBRE);
            filtros.put("nombre", "%" + citaTatto.getInfoCliente().toUpperCase() + "%");
        }
        if (citaTatto.getHora() != null && !citaTatto.getHora().isEmpty()) {
            sb.append(Constants.CITA_TATTO_HORA);
            filtros.put("hora", citaTatto.getHora());
        }

        if (citaTatto.getFechaCita() != null) {
            Date date = Date.from(citaTatto.getFechaCita());
            String fecha = format.format(date);
            sb.append(Constants.CITA_TATTO_FECHA);
            filtros.put("fechaCita", fecha);
        }

        sb.append(Constants.ORDENAR_CITAS_PORfECHA);

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List<CitaTatto> citas = q.getResultList();

        return citaTattoMapper.toDto(citas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaTattoDTO> citasPorFecha(String fechaCita) {
        log.debug("Request to get all citas tatto per date");

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String fechaCitaFormat = fechaCita.substring(0, fechaCita.indexOf("T"));

        Query q = entityManager.createQuery(Constants.CITA_TATTO_FECHA).setParameter("fechaCita", fechaCitaFormat);

        List<CitaTatto> citasFecha = q.getResultList();

        return citaTattoMapper.toDto(citasFecha);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaTattoDTO> findOne(Long id) {
        log.debug("Request to get CitaTatto : {}", id);

        return citaTattoRepository.findById(id).map(citaTattoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitaTatto : {}", id);
        // BigDecimal valor = abonoRepository.valorAbono(id);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Abono> abonosPorCita = abonoRepository.abonosPorCita(id);

        for (Abono abono : abonosPorCita) {
            Date form = Date.from(abono.getFechaAbono());
            String fecha = format.format(form);
            Query qu = entityManager
                .createQuery(Constants.ELIMINAR_VALORES_CAJA_POR_CITA)
                .setParameter("valorDescontar", abono.getValorAbono())
                .setParameter("fechaAbono", fecha);
            qu.executeUpdate();
        }

        Query q = entityManager.createQuery("DELETE FROM Abono WHERE idCita = :idCita").setParameter("idCita", id);

        q.executeUpdate();

        citaTattoRepository.deleteById(id);
    }

    @Override
    public byte[] generarReporteCitas() {
        Document documento = null;
        String nombreCita = null;

        // Se declaran las 2 fechas con fecha actual
        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();

        // Se resta un mes a la fecha fin
        fechaFin.add(Calendar.MONTH, -1);

        // SE TRANSFORMAN LAS FECHAS A INSTANT
        Instant fechaIni = fechaInicio.toInstant();
        Instant fechaFn = fechaFin.toInstant();

        List<CitaTatto> citaTattos = citaTattoRepository.citasMensuales(fechaFn, fechaIni);

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            documento = new Document();
            nombreCita = "Reporte " + citaTattos.size() + ".pdf";
            FileOutputStream fileOutput = new FileOutputStream(nombreCita);
            PdfWriter.getInstance(documento, fileOutput);
            documento.open();

            Paragraph titulo = new Paragraph("Informacion citas mensuales");
            titulo.setAlignment(1);

            documento.add(titulo);

            Date date1 = Date.from(fechaFn);
            Date date2 = Date.from(fechaIni);

            String citas1 = format.format(date1);
            String citas2 = format.format(date2);

            Paragraph infoCitas = new Paragraph("De la fecha: " + citas1);
            infoCitas.setAlignment(2);
            Paragraph infoCitas2 = new Paragraph("A la fecha: " + citas2);
            infoCitas2.setAlignment(2);
            infoCitas.setAlignment(2);
            documento.add(infoCitas);
            documento.add(infoCitas2);
            documento.add(Chunk.NEWLINE);

            Paragraph text = new Paragraph();
            text.add(
                "Si alguna cita no tiene valor pagado, vaya a el detalle de la cita del cliente y" +
                " revise sus abonos, esto se puede deber a que la cita no fue cancelada al momento" +
                "de crearla, de esta manera el valor pagado ira a la lista de abonos de la cita."
            );
            text.setAlignment(1);
            documento.add(text);

            documento.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            PdfPCell name = new PdfPCell(new Phrase("Nombre"));
            name.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell fechaCita = new PdfPCell(new Phrase("Fecha de la cita"));
            fechaCita.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell hora = new PdfPCell(new Phrase("Hora"));
            hora.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell valor = new PdfPCell(new Phrase("Valor del tattoo"));
            valor.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell pago = new PdfPCell(new Phrase("Valor Pagado"));
            pago.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell deuda = new PdfPCell(new Phrase("Deuda"));
            deuda.setBackgroundColor(BaseColor.ORANGE);

            table.addCell(name);
            table.addCell(fechaCita);
            table.addCell(hora);
            table.addCell(valor);
            table.addCell(pago);
            table.addCell(deuda);

            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal valorDeuda = BigDecimal.ZERO;

            for (CitaTatto citas : citaTattos) {
                table.addCell(citas.getInfoCliente());
                Date date = Date.from(citas.getFechaCita());
                String fecha = format.format(date);
                table.addCell(fecha);
                table.addCell(citas.getHora());
                table.addCell(citas.getValorTatto().toString());
                table.addCell(citas.getValorPagado().toString());
                table.addCell(citas.getDeuda().toString());
                valorTotal = valorTotal.add(citas.getValorTatto());
                valorDeuda = valorDeuda.add(citas.getDeuda());
            }

            documento.add(table);

            documento.add(Chunk.NEWLINE);
            Paragraph ValTotal = new Paragraph("Valor Total Del Mes: " + valorTotal);
            ValTotal.setAlignment(2);
            documento.add(ValTotal);

            Paragraph valDeuda = new Paragraph("Valor Deuda Mensual De Tattoos: " + valorDeuda);
            valDeuda.setAlignment(2);
            documento.add(valDeuda);

            log.info("info generacion arhivo despues del close y delte" + nombreCita);
            documento.close();

            return Files.readAllBytes(Paths.get(nombreCita));
        } catch (Exception e) {
            log.info("info generacion archivo: " + e);
            log.info("info generacion archivo: " + e.getMessage());
            log.info("info generacion archivo: " + e.getStackTrace());
            log.error("Error generacion archivo: " + e);
            log.error("Error generacion archivo: " + e.getMessage());
            log.error("Error generacion archivo: " + e.getStackTrace());
            e.printStackTrace();
            return null;
        }
    }

    // 0 0 6 * * ?
    @Scheduled(cron = "0 0 8 * * *")
    public void recordatorioCitas() {
        log.debug("Request to send mails");

        List<CitaTatto> citas = citaTattoRepository.recordatorioCitas();

        for (CitaTatto cita : citas) {
            ZonedDateTime zdt = ZonedDateTime.ofInstant(cita.getFechaCita(), ZoneId.systemDefault());
            Calendar cal = GregorianCalendar.from(zdt);

            // SE RESTA UN DIA A LA FECHA DE LA CITA.
            cal.add(Calendar.DATE, -1);
            Calendar fechaActual = Calendar.getInstance();

            int resp = cal.compareTo(fechaActual);

            if (resp == -1) {
                enviarCorreoRecordatorio(cita);
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void cambiarEstadoCita() {
        log.debug("Request to change estado cita");

        List<CitaTatto> citaTattos = citaTattoRepository.findAll();

        for (CitaTatto cita : citaTattos) {
            if (cita.getFechaCita().isBefore(Instant.now())) {
                cita.setEstadoCita("Finalizada");
            } else {
                cita.setEstado("Pendiente");
            }
            citaTattoRepository.save(cita);
        }
    }

    private void enviarCorreoRecordatorio(CitaTatto citaTattoo) {
        if (citaTattoo.getEmailCliente() != null && !citaTattoo.getEmailCliente().isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String mensaje =
                "<html><head><title>Recordatorio Cita Tattoo Correo</title></head>" +
                "<body style='background-color:black;'><p style='color:aqua;'>Buen dia " +
                citaTattoo.getInfoCliente() +
                ".<br/>" +
                "Recuerde que tiene una cita pendiente para el dia de mañana" +
                format.format(Date.from(citaTattoo.getFechaCita())) +
                " a las " +
                citaTattoo.getHora() +
                ".</p>" +
                "<p style='color:aqua;'>Recuerde llegar 10-15 minutos antes para la toma de medidas." +
                "<br/>Muchas Gracias.<br/><br/><br/>Preciado Tattoo.</p></body></html>";

            try {
                log.debug("Request to send mail to client: " + citaTattoo.getEmailCliente());
                EmailUtil.sendArchivoTLS(null, citaTattoo.getEmailCliente().trim(), "Confirmacion Cita Tattoo", mensaje);
                log.debug("Send email to: " + citaTattoo.getEmailCliente());
            } catch (Exception e) {
                log.debug("Error enviando notificacion ", e);
                log.debug("Error enviando notificacion ", e.getMessage());
                log.error("Error enviando notificacion ", e);
                log.error("Error enviando notificacion ", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
