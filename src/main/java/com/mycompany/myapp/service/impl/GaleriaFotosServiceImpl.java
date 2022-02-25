package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.GaleriaFotos;
import com.mycompany.myapp.repository.GaleriaFotosRepository;
import com.mycompany.myapp.service.GaleriaFotosService;
import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
import com.mycompany.myapp.service.mapper.GaleriaFotosMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GaleriaFotos}.
 */
@Service
@Transactional
public class GaleriaFotosServiceImpl implements GaleriaFotosService {

    private final Logger log = LoggerFactory.getLogger(GaleriaFotosServiceImpl.class);

    private final GaleriaFotosRepository galeriaFotosRepository;

    private final GaleriaFotosMapper galeriaFotosMapper;

    public GaleriaFotosServiceImpl(GaleriaFotosRepository galeriaFotosRepository, GaleriaFotosMapper galeriaFotosMapper) {
        this.galeriaFotosRepository = galeriaFotosRepository;
        this.galeriaFotosMapper = galeriaFotosMapper;
    }

    @Override
    public GaleriaFotosDTO save(GaleriaFotosDTO galeriaFotosDTO) {
        log.debug("Request to save GaleriaFotos : {}", galeriaFotosDTO);
        GaleriaFotos galeriaFotos = galeriaFotosMapper.toEntity(galeriaFotosDTO);
        galeriaFotos = galeriaFotosRepository.save(galeriaFotos);
        return galeriaFotosMapper.toDto(galeriaFotos);
    }

    @Override
    public Optional<GaleriaFotosDTO> partialUpdate(GaleriaFotosDTO galeriaFotosDTO) {
        log.debug("Request to partially update GaleriaFotos : {}", galeriaFotosDTO);

        return galeriaFotosRepository
            .findById(galeriaFotosDTO.getId())
            .map(existingGaleriaFotos -> {
                galeriaFotosMapper.partialUpdate(existingGaleriaFotos, galeriaFotosDTO);

                return existingGaleriaFotos;
            })
            .map(galeriaFotosRepository::save)
            .map(galeriaFotosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GaleriaFotosDTO> findAll() {
        log.debug("Request to get all GaleriaFotos");
        return galeriaFotosRepository.findAll().stream().map(galeriaFotosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GaleriaFotosDTO> findOne(Long id) {
        log.debug("Request to get GaleriaFotos : {}", id);
        return galeriaFotosRepository.findById(id).map(galeriaFotosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GaleriaFotos : {}", id);
        galeriaFotosRepository.deleteById(id);
    }
}
