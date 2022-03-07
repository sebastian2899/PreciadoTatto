package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CajaIngresos;
import com.mycompany.myapp.repository.CajaIngresosRepository;
import com.mycompany.myapp.service.CajaIngresosService;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import com.mycompany.myapp.service.dto.RegistroHistoricoCajaDTO;
import com.mycompany.myapp.service.mapper.CajaIngresosMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
}
