package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Ventas;
import com.mycompany.myapp.repository.VentasRepository;
import com.mycompany.myapp.service.VentasService;
import com.mycompany.myapp.service.dto.VentasDTO;
import com.mycompany.myapp.service.mapper.VentasMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ventas}.
 */
@Service
@Transactional
public class VentasServiceImpl implements VentasService {

    private final Logger log = LoggerFactory.getLogger(VentasServiceImpl.class);

    private final VentasRepository ventasRepository;

    private final VentasMapper ventasMapper;

    public VentasServiceImpl(VentasRepository ventasRepository, VentasMapper ventasMapper) {
        this.ventasRepository = ventasRepository;
        this.ventasMapper = ventasMapper;
    }

    @Override
    public VentasDTO save(VentasDTO ventasDTO) {
        log.debug("Request to save Ventas : {}", ventasDTO);
        Ventas ventas = ventasMapper.toEntity(ventasDTO);
        ventas = ventasRepository.save(ventas);
        return ventasMapper.toDto(ventas);
    }

    @Override
    public Optional<VentasDTO> partialUpdate(VentasDTO ventasDTO) {
        log.debug("Request to partially update Ventas : {}", ventasDTO);

        return ventasRepository
            .findById(ventasDTO.getId())
            .map(existingVentas -> {
                ventasMapper.partialUpdate(existingVentas, ventasDTO);

                return existingVentas;
            })
            .map(ventasRepository::save)
            .map(ventasMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentasDTO> findAll() {
        log.debug("Request to get all Ventas");
        return ventasRepository.findAll().stream().map(ventasMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VentasDTO> findOne(Long id) {
        log.debug("Request to get Ventas : {}", id);
        return ventasRepository.findById(id).map(ventasMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ventas : {}", id);
        ventasRepository.deleteById(id);
    }
}
