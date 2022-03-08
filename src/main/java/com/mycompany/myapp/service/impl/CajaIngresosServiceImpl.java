package com.mycompany.myapp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.domain.CajaIngresos;
import com.mycompany.myapp.repository.CajaIngresosRepository;
import com.mycompany.myapp.service.CajaIngresosService;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import com.mycompany.myapp.service.dto.RegistroHistoricoCajaDTO;
import com.mycompany.myapp.service.mapper.CajaIngresosMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.hibernate.hql.spi.id.AbstractTableBasedBulkIdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CajaIngresos}.
 */
@Service
@Transactional
public class CajaIngresosServiceImpl implements CajaIngresosService {

    private final Logger log = LoggerFactory.getLogger(CajaIngresosServiceImpl.class);

    private final CajaIngresosRepository cajaIngresosRepository;

    private final CajaIngresosMapper cajaIngresosMapper;

    public CajaIngresosServiceImpl(CajaIngresosRepository cajaIngresosRepository, CajaIngresosMapper cajaIngresosMapper) {
        this.cajaIngresosRepository = cajaIngresosRepository;
        this.cajaIngresosMapper = cajaIngresosMapper;
    }

    @Override
    public CajaIngresosDTO save(CajaIngresosDTO cajaIngresosDTO) {
        log.debug("Request to save CajaIngresos : {}", cajaIngresosDTO);
        CajaIngresos cajaIngresos = cajaIngresosMapper.toEntity(cajaIngresosDTO);

        if (cajaIngresos.getId() == null) {
            cajaIngresos.setFechaCreacion(Instant.now());
        }

        cajaIngresos = cajaIngresosRepository.save(cajaIngresos);
        return cajaIngresosMapper.toDto(cajaIngresos);
    }

    @Override
    public Optional<CajaIngresosDTO> partialUpdate(CajaIngresosDTO cajaIngresosDTO) {
        log.debug("Request to partially update CajaIngresos : {}", cajaIngresosDTO);

        return cajaIngresosRepository
            .findById(cajaIngresosDTO.getId())
            .map(existingCajaIngresos -> {
                cajaIngresosMapper.partialUpdate(existingCajaIngresos, cajaIngresosDTO);

                return existingCajaIngresos;
            })
            .map(cajaIngresosRepository::save)
            .map(cajaIngresosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaIngresosDTO> findAll() {
        log.debug("Request to get all CajaIngresos");
        return cajaIngresosRepository.findAll().stream().map(cajaIngresosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaIngresosDTO> findOne(Long id) {
        log.debug("Request to get CajaIngresos : {}", id);
        return cajaIngresosRepository.findById(id).map(cajaIngresosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal valoresDia() {
        log.debug("Request to get price of cita perfo");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = format.format(new Date());

        BigDecimal valorPerfo = cajaIngresosRepository.valorCitas(fechaActual);
        if (valorPerfo == null) {
            valorPerfo = BigDecimal.ZERO;
        }

        BigDecimal valorVenta = cajaIngresosRepository.valorVentaDia(fechaActual);

        if (valorVenta == null) {
            valorVenta = BigDecimal.ZERO;
        }

        BigDecimal valorAbonos = cajaIngresosRepository.valorTotalAbonos(fechaActual);
        if (valorAbonos == null) {
            valorAbonos = BigDecimal.ZERO;
        }

        BigDecimal valorTotal = valorPerfo.add(valorAbonos).add(valorVenta);

        return valorTotal;
    }

    @Override
    public RegistroHistoricoCajaDTO cajaIngresosFecha(Instant fechaInicio, Instant fechaFin) {
        log.debug("Request to get CajaIngresos for Dates");

        List<CajaIngresos> cajaFechas = cajaIngresosRepository.cajaFechas(fechaInicio, fechaFin);
        RegistroHistoricoCajaDTO rhcd = new RegistroHistoricoCajaDTO();

        BigDecimal valorVendido = BigDecimal.ZERO;
        BigDecimal valorPagado = BigDecimal.ZERO;
        BigDecimal diferencia = BigDecimal.ZERO;

        for (CajaIngresos caja : cajaFechas) {
            valorVendido = valorVendido.add(caja.getValorVendidoDia());
            valorPagado = valorPagado.add(caja.getValorVendidoDia());
            diferencia = diferencia.add(caja.getDiferencia());
        }

        rhcd.setValorPagado(valorPagado);
        rhcd.setValorVendido(valorVendido);
        rhcd.setDiferencia(diferencia);

        return rhcd;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CajaIngresos : {}", id);
        cajaIngresosRepository.deleteById(id);
    }

    @Override
    public byte[] reporteCajaIngresoMensual() {
        log.debug("Request to generate report monthly");

        Document document = null;
        String nombreDocumento = null;

        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();
        fechaFin.add(Calendar.MONTH, -1);

        Instant fechaIni = fechaFin.toInstant();
        Instant fechaFn = fechaInicio.toInstant();

        List<CajaIngresos> cajaPorFechas = cajaIngresosRepository.cajaFechas(fechaIni, fechaFn);

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            document = new Document();
            nombreDocumento = "Reporte Caja Ingreso Mensual";
            FileOutputStream fileOutputStream = new FileOutputStream(nombreDocumento);
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            Paragraph titulo = new Paragraph("Reporte Caja Ingreso Mensual");
            titulo.setAlignment(1);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            Date fecha1 = Date.from(fechaIni);
            Date fecha2 = Date.from(fechaFn);

            String f1 = format.format(fecha1);
            String f2 = format.format(fecha2);

            Paragraph fechaInicial = new Paragraph("Desde la fecha: " + f1.toString());
            fechaInicial.setAlignment(2);
            document.add(fechaInicial);

            Paragraph fechaF = new Paragraph("Hasta la fecha: " + f2.toString());
            fechaF.setAlignment(2);
            document.add(fechaF);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            PdfPCell fechaCreacion = new PdfPCell(new Paragraph("Fecha Creacion Caja"));
            fechaCreacion.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(fechaCreacion);
            PdfPCell valorVendido = new PdfPCell(new Paragraph("Valor vendido en el dia"));
            valorVendido.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(valorVendido);
            PdfPCell valorPagadoDia = new PdfPCell(new Paragraph("Valor registrado del dia"));
            valorPagadoDia.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(valorPagadoDia);
            PdfPCell diferencia = new PdfPCell(new Paragraph("Diferencia Caja"));
            diferencia.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(diferencia);

            BigDecimal valorTotal = BigDecimal.ZERO;
            BigDecimal diferenciaCaja = BigDecimal.ZERO;

            for (CajaIngresos caja : cajaPorFechas) {
                Date date = Date.from(caja.getFechaCreacion());
                String fecha = format.format(date);
                table.addCell(fecha);
                table.addCell(caja.getValorVendidoDia().toString());
                table.addCell(caja.getValorRegistradoDia().toString());
                table.addCell(caja.getDiferencia().toString());
                valorTotal = valorTotal.add(caja.getValorRegistradoDia());
                diferenciaCaja = diferenciaCaja.add(caja.getDiferencia());
            }

            document.add(table);
            document.add(Chunk.NEWLINE);
            table = new PdfPTable(1);
            PdfPCell cell = new PdfPCell(new Paragraph("Valor Total Caja Mes: " + valorTotal.toString()));
            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(1);
            cell = new PdfPCell(new Paragraph("Diferencia Mes: " + diferenciaCaja.toString()));
            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);

            document.add(table);

            log.info("info generacion arhivo despues del close y delte" + nombreDocumento);
            document.close();

            return FileUtils.readFileToByteArray(new File(nombreDocumento));
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
