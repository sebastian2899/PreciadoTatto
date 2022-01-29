package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CajaTattos;
import com.mycompany.myapp.repository.CajaTattosRepository;
import com.mycompany.myapp.service.CajaTattosService;
import com.mycompany.myapp.service.dto.CajaTattosDTO;
import com.mycompany.myapp.service.mapper.CajaTattosMapper;
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
}
