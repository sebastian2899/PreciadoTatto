package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Egreso;
import com.mycompany.myapp.repository.EgresoRepository;
import com.mycompany.myapp.service.EgresoService;
import com.mycompany.myapp.service.dto.EgresoDTO;
import com.mycompany.myapp.service.mapper.EgresoMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    public List<EgresoDTO> egresosDiarios() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        return egresoRepository
            .listaEgresoDiario(fecha)
            .stream()
            .map(egresoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
