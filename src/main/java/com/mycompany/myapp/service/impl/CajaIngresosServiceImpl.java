package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CajaIngresos;
import com.mycompany.myapp.repository.CajaIngresosRepository;
import com.mycompany.myapp.service.CajaIngresosService;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import com.mycompany.myapp.service.mapper.CajaIngresosMapper;
import java.time.Instant;
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
    public void delete(Long id) {
        log.debug("Request to delete CajaIngresos : {}", id);
        cajaIngresosRepository.deleteById(id);
    }
}
