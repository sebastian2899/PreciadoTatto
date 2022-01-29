package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CitaPerforacion;
import com.mycompany.myapp.repository.CitaPerforacionRepository;
import com.mycompany.myapp.service.CitaPerforacionService;
import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import com.mycompany.myapp.service.mapper.CitaPerforacionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public CitaPerforacionServiceImpl(CitaPerforacionRepository citaPerforacionRepository, CitaPerforacionMapper citaPerforacionMapper) {
        this.citaPerforacionRepository = citaPerforacionRepository;
        this.citaPerforacionMapper = citaPerforacionMapper;
    }

    @Override
    public CitaPerforacionDTO save(CitaPerforacionDTO citaPerforacionDTO) {
        log.debug("Request to save CitaPerforacion : {}", citaPerforacionDTO);
        CitaPerforacion citaPerforacion = citaPerforacionMapper.toEntity(citaPerforacionDTO);
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
        return citaPerforacionRepository
            .findAll()
            .stream()
            .map(citaPerforacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaPerforacionDTO> findOne(Long id) {
        log.debug("Request to get CitaPerforacion : {}", id);
        return citaPerforacionRepository.findById(id).map(citaPerforacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitaPerforacion : {}", id);
        citaPerforacionRepository.deleteById(id);
    }
}
