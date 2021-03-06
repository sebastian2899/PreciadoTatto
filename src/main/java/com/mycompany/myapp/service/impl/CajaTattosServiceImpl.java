package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CajaTattos;
import com.mycompany.myapp.repository.CajaTattosRepository;
import com.mycompany.myapp.service.CajaTattosService;
import com.mycompany.myapp.service.dto.CajaTattosDTO;
import com.mycompany.myapp.service.dto.RegistroHistoricoCajaDTO;
import com.mycompany.myapp.service.mapper.CajaTattosMapper;
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
 * Service Implementation for managing {@link CajaTattos}.
 */
@Service
@Transactional
public class CajaTattosServiceImpl implements CajaTattosService {

    private final Logger log = LoggerFactory.getLogger(CajaTattosServiceImpl.class);

    private final CajaTattosRepository cajaTattosRepository;

    private final CajaTattosMapper cajaTattosMapper;

    public CajaTattosServiceImpl(CajaTattosRepository cajaTattosRepository, CajaTattosMapper cajaTattosMapper) {
        this.cajaTattosRepository = cajaTattosRepository;
        this.cajaTattosMapper = cajaTattosMapper;
    }

    @Override
    public CajaTattosDTO save(CajaTattosDTO cajaTattosDTO) {
        log.debug("Request to save CajaTattos : {}", cajaTattosDTO);
        CajaTattos cajaTattos = cajaTattosMapper.toEntity(cajaTattosDTO);

        if (cajaTattos.getId() == null) {
            cajaTattos.setFechaCreacion(Instant.now());
        }

        cajaTattos = cajaTattosRepository.save(cajaTattos);
        return cajaTattosMapper.toDto(cajaTattos);
    }

    @Override
    public Optional<CajaTattosDTO> partialUpdate(CajaTattosDTO cajaTattosDTO) {
        log.debug("Request to partially update CajaTattos : {}", cajaTattosDTO);

        return cajaTattosRepository
            .findById(cajaTattosDTO.getId())
            .map(existingCajaTattos -> {
                cajaTattosMapper.partialUpdate(existingCajaTattos, cajaTattosDTO);

                return existingCajaTattos;
            })
            .map(cajaTattosRepository::save)
            .map(cajaTattosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaTattosDTO> findAll() {
        log.debug("Request to get all CajaTattos");
        return cajaTattosRepository.findAll().stream().map(cajaTattosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaTattosDTO> findOne(Long id) {
        log.debug("Request to get CajaTattos : {}", id);
        return cajaTattosRepository.findById(id).map(cajaTattosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CajaTattos : {}", id);
        cajaTattosRepository.deleteById(id);
    }

    @Override
    public BigDecimal consultarValoresDiariosTattos() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        BigDecimal valorAbonoDia = cajaTattosRepository.valorAbonoDia(fecha);

        if (valorAbonoDia == null) {
            valorAbonoDia = BigDecimal.ZERO;
        }

        BigDecimal totalDia = valorAbonoDia;

        //BigDecimal valorTotalDia = valorTattosPagados.add(valorAbonoDiario);

        return totalDia;
    }

    @Override
    @Transactional(readOnly = true)
    public RegistroHistoricoCajaDTO registroCaja(Instant fechaInciio, Instant fechaFin) {
        RegistroHistoricoCajaDTO rhcd = new RegistroHistoricoCajaDTO();

        List<CajaTattos> cajas = cajaTattosRepository.cajaTattoFecha(fechaInciio, fechaFin);

        BigDecimal valorVendidoDia = BigDecimal.ZERO;
        BigDecimal valorPagado = BigDecimal.ZERO;
        BigDecimal diferencia = BigDecimal.ZERO;

        //ANTES
        /*for (CajaTattos cajaTattos : cajas) {
			valorVendidoDia.add(cajaTattos.getValorTattoDia());
			valorPagado.add(cajaTattos.getValorRegistrado());
			diferencia.add(cajaTattos.getDiferencia());
		}
		*/

        //DESPUES (funciona)
        for (CajaTattos cajaTattos : cajas) {
            valorVendidoDia = valorVendidoDia.add(cajaTattos.getValorTattoDia());
            valorPagado = valorPagado.add(cajaTattos.getValorRegistrado());
            diferencia = diferencia.add(cajaTattos.getDiferencia());
        }

        rhcd.setValorVendido(valorVendidoDia);
        rhcd.setValorPagado(valorPagado);
        rhcd.setDiferencia(diferencia);

        return rhcd;
    }
}
