package com.mycompany.myapp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
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
import com.mycompany.myapp.service.mapper.CitaTattoMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                abono.setValorAbono(citaTatto.getValorPagado());

                this.abonoRepository.save(abono);
            }
        }

        return citaTattoMapper.toDto(citaTatto);
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

        sb.append(Constants.CITA_TATTO_BASE);
        if (citaTatto.getInfoCliente() != null && !citaTatto.getInfoCliente().isEmpty()) {
            sb.append(Constants.CITA_TATTO_NOMBRE);
            filtros.put("nombre", "%" + citaTatto.getInfoCliente().toUpperCase() + "%");
        }
        if (citaTatto.getHora() != null && !citaTatto.getHora().isEmpty()) {
            sb.append(Constants.CITA_TATTO_HORA);
            filtros.put("hora", citaTatto.getHora());
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List<CitaTatto> citas = q.getResultList();

        return citaTattoMapper.toDto(citas);
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
        File archivo = null;
        String nombreCita = null;

        //Se declaran las 2 fechas con fecha actual
        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();

        //Se resta un mes a la fecha fin
        fechaFin.add(Calendar.MONTH, -1);

        //SE TRANSFORMAN LAS FECHAS A INSTANT
        Instant fechaIni = fechaInicio.toInstant();
        Instant fechaFn = fechaFin.toInstant();

        List<CitaTatto> citaTattos = citaTattoRepository.citasMensuales(fechaFn, fechaIni);

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            documento = new Document();
            nombreCita = "Reporte: " + citaTattos.size() + ".pdf";
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

            return FileUtils.readFileToByteArray(new File(nombreCita));
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
}
