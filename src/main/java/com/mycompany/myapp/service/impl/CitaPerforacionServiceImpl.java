package com.mycompany.myapp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Abono;
import com.mycompany.myapp.domain.CitaPerforacion;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.AbonoRepository;
import com.mycompany.myapp.repository.CitaPerforacionRepository;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.CitaPerforacionService;
import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import com.mycompany.myapp.service.dto.MensajeValidacionCitaDTO;
import com.mycompany.myapp.service.mapper.CitaPerforacionMapper;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CitaPerforacion}.
 */
@Service
@Transactional
public class CitaPerforacionServiceImpl implements CitaPerforacionService {

    private final Logger log = LoggerFactory.getLogger(CitaPerforacionServiceImpl.class);

    private final CitaPerforacionRepository citaPerforacionRepository;

    private final CitaPerforacionMapper citaPerforacionMapper;

    private final AbonoRepository abonoRepository;

    private final CitaTattoRepository citaTattoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CitaPerforacionServiceImpl(
        CitaPerforacionRepository citaPerforacionRepository,
        AbonoRepository abonoRepository,
        CitaPerforacionMapper citaPerforacionMapper,
        CitaTattoRepository citaTattoRepository
    ) {
        this.citaPerforacionRepository = citaPerforacionRepository;
        this.citaPerforacionMapper = citaPerforacionMapper;
        this.abonoRepository = abonoRepository;
        this.citaTattoRepository = citaTattoRepository;
    }

    @Override
    public CitaPerforacionDTO save(CitaPerforacionDTO citaPerforacionDTO) {
        log.debug("Request to save CitaPerforacion : {}", citaPerforacionDTO);
        CitaPerforacion citaPerforacion = citaPerforacionMapper.toEntity(citaPerforacionDTO);
        citaPerforacion.setFechaCreacionInicial(Instant.now());

        citaPerforacion.setEstadoCita(citaPerforacion.getFechaCita().isAfter(Instant.now()) ? "Pendiente" : "Finalizaada");

        citaPerforacion.setEstado(citaPerforacionDTO.getValorDeuda().equals(BigDecimal.ZERO) ? "Pagada" : "Deuda");

        citaPerforacion.setFechaCreacion(citaPerforacion.getValorPagado() == null ? null : Instant.now());

        citaPerforacion = citaPerforacionRepository.save(citaPerforacion);

        return citaPerforacionMapper.toDto(citaPerforacion);
    }

    @Override
    public Optional<CitaPerforacionDTO> partialUpdate(CitaPerforacionDTO citaPerforacionDTO) {
        log.debug("Request to partially update CitaPerforacion : {}", citaPerforacionDTO);

        return citaPerforacionRepository
            .findById(citaPerforacionDTO.getId())
            .map(existingCitaPerforacion -> {
                citaPerforacionMapper.partialUpdate(existingCitaPerforacion, citaPerforacionDTO);

                return existingCitaPerforacion;
            })
            .map(citaPerforacionRepository::save)
            .map(citaPerforacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaPerforacionDTO> findAll() {
        log.debug("Request to get all CitaPerforacions");

        Query q = entityManager.createQuery(Constants.ORDENAR_CITASPERF_PORFECHA);

        List<CitaPerforacion> citasOrder = q.getResultList();

        return citaPerforacionMapper.toDto(citasOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaPerforacionDTO> findCitasToday() {
        log.debug("Request to get all CitaPerforacions today");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        List<CitaPerforacion> citasHoy = citaPerforacionRepository.citasHoy(fecha);

        return citaPerforacionMapper.toDto(citasHoy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaPerforacionDTO> citasPerfoPorFiltro(CitaPerforacionDTO citaPerfo) {
        log.debug("Request to get all citas per filters");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<>();

        sb.append(Constants.CITA_PERFORACION_BASE);

        if (citaPerfo.getNombreCliente() != null && !citaPerfo.getNombreCliente().isEmpty()) {
            sb.append(Constants.CITA_PERFORACION_NOMBRE);
            filtros.put("nombre", "%" + citaPerfo.getNombreCliente().toUpperCase() + "%");
        }
        if (citaPerfo.getHora() != null && !citaPerfo.getHora().isEmpty()) {
            sb.append(Constants.CITA_PERFORACION_HORA);
            filtros.put("hora", citaPerfo.getHora());
        }

        sb.append(Constants.ORDENAR_CITASPERF_PORFECHA);

        Query q = entityManager.createQuery(sb.toString());

        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List<CitaPerforacion> citas = q.getResultList();

        return citaPerforacionMapper.toDto(citas);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaPerforacionDTO> findOne(Long id) {
        log.debug("Request to get CitaPerforacion : {}", id);
        return citaPerforacionRepository.findById(id).map(citaPerforacionMapper::toDto);
    }

    @Override
    @Transactional
    public int consultarTipoCita(Long id) {
        log.debug("Request to get cita type");

        CitaPerforacion citaPerfo = citaPerforacionRepository.citaPorId(id);

        CitaTatto citaTattoo = citaTattoRepository.citaById(id);
        int resp = 0;

        // SI EXISTE LA CITA PERFORACION CON EL ID ENVIADO, SE RETORNA UN 1
        if (citaPerfo != null) {
            return 1;
        }

        // SI EXISTE LA CITA TATTOO CON EL ID ENVIADO, SE RETORNA UN 2
        if (citaTattoo != null) {
            resp = 2;
        }

        return resp;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitaPerforacion : {}", id);

        CitaPerforacion citaPerfo = citaPerforacionRepository.citaPorId(id);
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Map<String, Object> filtros = new HashMap<>();

        List<Abono> abonosPorCita = abonoRepository.abonosPorCita(id);

        BigDecimal valorTotalDescontar = BigDecimal.ZERO;

        /*
         * SE VALIDA QUE EXISTA UNA FECHA DE CREACION DE LA CITA DONDE HAYA SIDO PAGADA Y SE VALIDA QUE EXISTA UN VALOR PAGADO.
         * SE AGREGA AL MAPA EL NOMBRE Y EL VALOR DE EL PARAMETRO QUE IRA A LA CONSULTA
         */

        if (citaPerfo.getFechaCreacion() != null) {
            Date fechaDate = Date.from(citaPerfo.getFechaCreacion());
            String fecha = format.format(fechaDate);
            filtros.put("fechaCita", fecha);
        }

        if (citaPerfo.getValorCaja() != null && !(citaPerfo.getValorCaja().equals(new BigDecimal(0)))) {
            valorTotalDescontar = valorTotalDescontar.add(citaPerfo.getValorCaja());
            filtros.put("descontar", valorTotalDescontar);
        }

        //CON LOS VALORES RECOLECTADOS POR LA VALIDACION SE AGREGA AL STRING BUILDER LA CONSULTA QUE RESTARA LOS VALORES A LA CAJA.
        sb.append(Constants.ELIMINAR_VALORES_CAJA_POR_CITA_PERFO);

        //SI EXISTE ALGUN ABONO REFERENTE A LA CITA
        if (abonosPorCita != null) {
            for (Abono abono : abonosPorCita) {
                if (abono.getValorAbono() != null && !(abono.getValorAbono().equals(new BigDecimal(0)))) {
                    valorTotalDescontar.add(citaPerfo.getValorCaja());
                    filtros.put("descontar", valorTotalDescontar);
                }

                if (abono.getFechaAbono() != null) {
                    Date dateFecha = Date.from(abono.getFechaAbono());
                    String fechaAbono = format.format(dateFecha);
                    sb.append(Constants.ELIMINAR_VALORES_CAJA_POR_CITA_PERFO_ABONO);
                    filtros.put("abonofechaPerfo", fechaAbono);
                }
            }
        }

        Query qu = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            qu.setParameter(filtro.getKey(), filtro.getValue());
        }

        qu.executeUpdate();

        Query q = entityManager.createQuery(Constants.ELIMINAR_ABONOS_POR_CITA).setParameter("idCita", id);

        q.executeUpdate();

        citaPerforacionRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 8 * * *")
    //0 0 8 * * *
    public void cambiarEstadoCita() {
        log.debug("Request to change state cita");

        List<CitaPerforacion> citas = citaPerforacionRepository.findAll();
        Instant fechaActual = Instant.now();

        for (CitaPerforacion cita : citas) {
            if (cita.getFechaCita().isAfter(fechaActual)) {
                cita.setEstadoCita("Pendiente");
            } else {
                cita.setEstadoCita("Finalizada");
            }

            citaPerforacionRepository.save(cita);
        }
    }

    @Override
    public MensajeValidacionCitaDTO mensajeValidacion(CitaPerforacionDTO citaPerforacionDTO) {
        log.debug("Request to validation save Cita Perforacion");

        MensajeValidacionCitaDTO mensaje = new MensajeValidacionCitaDTO();
        //SE FORMATEA LA HORA DEL TATTO PARA RECUPERAR SOLO EL PRIMER DIGITO PARA COMPARARLO DESPUES.
        /*
         * String horaC = citaPerforacionDTO.getHora().substring(0,1); int horaCita =
         * Integer.parseInt(horaC);
         */

        //SE RECUPERAN TODAS LAS CITAS DE MARIA FERNANDA QUE EXISTAN PARA VALIDAR CADA CITA CON LA NUEVA CITA QUE NOS ESTA LLEGANDO
        List<CitaPerforacion> citasPerfo = citaPerforacionRepository.findAll();

        //SE HACE LA VALIDACION CORRESPONDIENTE
        for (CitaPerforacion cita : citasPerfo) {
            /*
             * String hCita = cita.getHora().substring(0,1); int hCitaInt =
             * Integer.parseInt(hCita);
             */

            if (
                cita.getFechaCita().equals(citaPerforacionDTO.getFechaCita()) &&
                cita.getHora().equalsIgnoreCase(citaPerforacionDTO.getHora())
            ) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = Date.from(cita.getFechaCita());
                String fecha = format.format(date);
                mensaje.setMensaje(
                    "No se puede crear la cita para la fecha " +
                    fecha +
                    " debido a que ya hay una cita existente para" +
                    " esta fecha y hora ingresada ( " +
                    cita.getHora() +
                    " ) Por favor ingrese una fecha valida para la creacion de la cita."
                );
            }
        }

        return mensaje;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaPerforacionDTO> citasPorFecha(String fechaCita) {
        log.debug("Request to get citas per date");

        String fecha = fechaCita.substring(0, fechaCita.indexOf("T"));

        Query q = entityManager.createQuery(Constants.CITA_PERFORACION_FECHA).setParameter("fechaCita", fecha);

        List<CitaPerforacion> citasPorFecha = q.getResultList();

        return citaPerforacionMapper.toDto(citasPorFecha);
    }

    public byte[] generarReporteCitasPerfo() {
        log.debug("Request to generate report citas perfo.");

        Document document = null;
        String nombreDocumento = null;

        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();

        fechaInicio.add(Calendar.MONTH, -1);

        Instant fechaIni = fechaInicio.toInstant();
        Instant fechaF = fechaFin.toInstant();

        List<CitaPerforacion> citasPorMes = citaPerforacionRepository.citasPorFechas(fechaIni, fechaF);

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            document = new Document();
            nombreDocumento = "Reporte " + citasPorMes.size() + ".pdf";
            FileOutputStream fileOutput = new FileOutputStream(nombreDocumento);
            PdfWriter.getInstance(document, fileOutput);
            document.open();

            Paragraph titulo = new Paragraph("Reporte Citas Perforacion Mensuales");
            titulo.setAlignment(1);
            document.add(titulo);

            Date date1 = Date.from(fechaIni);
            Date date2 = Date.from(fechaF);

            String fecha1 = format.format(date1);
            String fecha2 = format.format(date2);

            Paragraph fechaIn = new Paragraph("De la fecha: " + fecha1);
            fechaIn.setAlignment(2);
            Paragraph fechaFn = new Paragraph("A la fecha: " + fecha2);
            fechaFn.setAlignment(2);

            document.add(fechaIn);
            document.add(fechaFn);

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            PdfPCell nombre = new PdfPCell(new Phrase("Cliente"));
            nombre.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell fechaCita = new PdfPCell(new Phrase("Fecha Cita"));
            fechaCita.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell hora = new PdfPCell(new Phrase("Hora"));
            hora.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell valorPerfo = new PdfPCell(new Phrase("Valor Perforacion"));
            valorPerfo.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell deuda = new PdfPCell(new Phrase("Deuda"));
            deuda.setBackgroundColor(BaseColor.ORANGE);

            table.addCell(nombre);
            table.addCell(fechaCita);
            table.addCell(hora);
            table.addCell(valorPerfo);
            table.addCell(deuda);

            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal valorDeuda = BigDecimal.ZERO;

            for (CitaPerforacion cita : citasPorMes) {
                table.addCell(cita.getNombreCliente());
                Date date = Date.from(cita.getFechaCita());
                String fecha = format.format(date);
                table.addCell(fecha);
                table.addCell(cita.getHora());
                table.addCell(cita.getValorPerforacion().toString());
                table.addCell(cita.getValorDeuda().toString());
                valorTotal = valorTotal.add(cita.getValorPerforacion());
                valorDeuda = valorDeuda.add(cita.getValorDeuda());
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Font fuente = new Font();
            fuente.setColor(BaseColor.ORANGE);

            table = new PdfPTable(1);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.ORANGE);
            Paragraph valTotal = new Paragraph("Valor Total: " + valorTotal.toString());
            valTotal.setAlignment(1);
            cell.addElement(valTotal);
            table.addCell(cell);
            document.add(table);
            table = new PdfPTable(1);
            table.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.ORANGE);
            Paragraph valDeuda = new Paragraph("Valor Deuda: " + valorDeuda.toString());
            valDeuda.setAlignment(1);
            cell.addElement(valDeuda);
            table.addCell(cell);
            document.add(table);

            log.info("info generacion arhivo despues del close y delte" + nombreDocumento);
            document.close();

            return Files.readAllBytes(Paths.get(nombreDocumento));
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
