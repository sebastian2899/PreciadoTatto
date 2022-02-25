package com.mycompany.myapp.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.myapp.domain.Egreso;
import com.mycompany.myapp.repository.EgresoRepository;
import com.mycompany.myapp.service.EgresoService;
import com.mycompany.myapp.service.dto.EgresoDTO;
import com.mycompany.myapp.service.mapper.EgresoMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egreso}.
 */
@Service
@Transactional
public class EgresoServiceImpl implements EgresoService {

    private final Logger log = LoggerFactory.getLogger(EgresoServiceImpl.class);

    private final EgresoRepository egresoRepository;

    private final EgresoMapper egresoMapper;

    public EgresoServiceImpl(EgresoRepository egresoRepository, EgresoMapper egresoMapper) {
        this.egresoRepository = egresoRepository;
        this.egresoMapper = egresoMapper;
    }

    @Override
    public EgresoDTO save(EgresoDTO egresoDTO) {
        log.debug("Request to save Egreso : {}", egresoDTO);
        Egreso egreso = egresoMapper.toEntity(egresoDTO);
        egreso = egresoRepository.save(egreso);
        return egresoMapper.toDto(egreso);
    }

    @Override
    public Optional<EgresoDTO> partialUpdate(EgresoDTO egresoDTO) {
        log.debug("Request to partially update Egreso : {}", egresoDTO);

        return egresoRepository
            .findById(egresoDTO.getId())
            .map(existingEgreso -> {
                egresoMapper.partialUpdate(existingEgreso, egresoDTO);

                return existingEgreso;
            })
            .map(egresoRepository::save)
            .map(egresoMapper::toDto);
    }

    @Override
    public BigDecimal valorEgresoDia() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = sdf.format(new Date());

        BigDecimal valorEgresoDia = egresoRepository.valorEgresoDiario(fecha);

        if (valorEgresoDia == null) {
            valorEgresoDia = BigDecimal.ZERO;
        }

        return valorEgresoDia;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgresoDTO> findAll() {
        log.debug("Request to get all Egresos");
        return egresoRepository.findAll().stream().map(egresoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EgresoDTO> findOne(Long id) {
        log.debug("Request to get Egreso : {}", id);
        return egresoRepository.findById(id).map(egresoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egreso : {}", id);
        egresoRepository.deleteById(id);
    }

    @Override
    public BigDecimal egresoMensual(Instant fechaInicio, Instant fechaFin) {
        log.debug("Request to get egreso monthly");

        BigDecimal egresoMensual = BigDecimal.ZERO;
        egresoMensual = egresoRepository.egresoMensual(fechaInicio, fechaFin);

        return egresoMensual;
    }

    @Override
    public List<EgresoDTO> egresosDiarios() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        return egresoRepository
            .listaEgresoDiario(fecha)
            .stream()
            .map(egresoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public byte[] generarReporteEgresoMensual() {
        log.debug("Request to generate report egreso");
        Document documento = null;
        String nombreDocumento = null;

        Calendar fechaInicio = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();

        fechaInicio.add(Calendar.MONTH, -1);

        Instant fechaIni = fechaInicio.toInstant();
        Instant fechaF = fechaFin.toInstant();

        List<Egreso> egresoMes = egresoRepository.reporteEgresoMensual(fechaIni, fechaF);

        try {
            documento = new Document();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            nombreDocumento = "Reporte " + egresoMes.size() + ".pdf";
            FileOutputStream fileOutput = new FileOutputStream(nombreDocumento);
            PdfWriter.getInstance(documento, fileOutput);
            documento.open();

            Paragraph titulo = new Paragraph("Reporte Egreso Mensual");
            titulo.setAlignment(1);

            documento.add(titulo);

            Date date = Date.from(fechaIni);
            Date date2 = Date.from(fechaF);

            String fechaI = format.format(date);
            String fechaFn = format.format(date2);

            Paragraph fi = new Paragraph("Del mes: " + fechaI);
            Paragraph ff = new Paragraph("Al mes: " + fechaFn);
            fi.setAlignment(2);
            ff.setAlignment(2);
            documento.add(fi);
            documento.add(ff);

            documento.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            PdfPCell fechaEgreso = new PdfPCell(new Phrase("Fecha del egreso"));
            fechaEgreso.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell descripcion = new PdfPCell(new Phrase("Descripcion del egreso"));
            descripcion.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell valor = new PdfPCell(new Phrase("Valor del egreso"));
            valor.setBackgroundColor(BaseColor.ORANGE);

            table.addCell(fechaEgreso);
            table.addCell(descripcion);
            table.addCell(valor);

            BigDecimal valorTotal = BigDecimal.ZERO;
            for (Egreso egreso : egresoMes) {
                Date dat = Date.from(egreso.getFechaCreacion());
                String fech = format.format(dat);
                table.addCell(fech);
                table.addCell(egreso.getDescripcion());
                if (egreso.getValor() == null) {
                    BigDecimal valr = BigDecimal.ZERO;
                    table.addCell(valr.toString());
                } else {
                    table.addCell(egreso.getValor().toString());
                    valorTotal = valorTotal.add(egreso.getValor());
                }
            }

            documento.add(table);

            table = new PdfPTable(1);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("Total Egreso Mensual: " + valorTotal.toString()));
            cell.setBackgroundColor(BaseColor.YELLOW);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            documento.add(table);

            log.debug("Generacion antes del close");
            documento.close();

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
