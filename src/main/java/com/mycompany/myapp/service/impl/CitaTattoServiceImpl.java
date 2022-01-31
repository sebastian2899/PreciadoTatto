package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.CitaTattoService;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
import com.mycompany.myapp.service.mapper.CitaTattoMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    private final CitaTattoMapper citaTattoMapper;

    public CitaTattoServiceImpl(CitaTattoRepository citaTattoRepository, CitaTattoMapper citaTattoMapper) {
        this.citaTattoRepository = citaTattoRepository;
        this.citaTattoMapper = citaTattoMapper;
    }

    @Override
    public CitaTattoDTO save(CitaTattoDTO citaTattoDTO) {
        log.debug("Request to save CitaTatto : {}", citaTattoDTO);
        CitaTatto citaTatto = citaTattoMapper.toEntity(citaTattoDTO);

        if (citaTatto.getDeuda().equals(BigDecimal.ZERO)) {
            citaTatto.estado("Pagada");
        }

        citaTatto = citaTattoRepository.save(citaTatto);
        return citaTattoMapper.toDto(citaTatto);
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

        List<CitaTatto> citas = citaTattoRepository.findAll();

        for (CitaTatto citaTatto : citas) {
            citaTatto.setNombreCliente(nombreCliente(citaTatto.getIdCliente()));
        }

        return citaTattoMapper.toDto(citas);
    }

    private String nombreCliente(Long id) {
        return citaTattoRepository.nombreCliente(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaTattoDTO> findOne(Long id) {
        log.debug("Request to get CitaTatto : {}", id);

        CitaTatto cita = citaTattoRepository.getById(id);
        cita.setNombreCliente(nombreCliente(id));

        return Optional.ofNullable(citaTattoMapper.toDto(cita));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitaTatto : {}", id);
        citaTattoRepository.deleteById(id);
    }
}
