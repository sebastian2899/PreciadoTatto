package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Compras;
import com.mycompany.myapp.repository.ComprasRepository;
import com.mycompany.myapp.service.ComprasService;
import com.mycompany.myapp.service.dto.ComprasDTO;
import com.mycompany.myapp.service.mapper.ComprasMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Compras}.
 */
@Service
@Transactional
public class ComprasServiceImpl implements ComprasService {

    private final Logger log = LoggerFactory.getLogger(ComprasServiceImpl.class);

    private final ComprasRepository comprasRepository;

    private final ComprasMapper comprasMapper;

    public ComprasServiceImpl(ComprasRepository comprasRepository, ComprasMapper comprasMapper) {
        this.comprasRepository = comprasRepository;
        this.comprasMapper = comprasMapper;
    }

    @Override
    public ComprasDTO save(ComprasDTO comprasDTO) {
        log.debug("Request to save Compras : {}", comprasDTO);
        Compras compras = comprasMapper.toEntity(comprasDTO);
        compras = comprasRepository.save(compras);
        return comprasMapper.toDto(compras);
    }

    @Override
    public Optional<ComprasDTO> partialUpdate(ComprasDTO comprasDTO) {
        log.debug("Request to partially update Compras : {}", comprasDTO);

        return comprasRepository
            .findById(comprasDTO.getId())
            .map(existingCompras -> {
                comprasMapper.partialUpdate(existingCompras, comprasDTO);

                return existingCompras;
            })
            .map(comprasRepository::save)
            .map(comprasMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprasDTO> findAll() {
        log.debug("Request to get all Compras");
        return comprasRepository.findAll().stream().map(comprasMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprasDTO> findOne(Long id) {
        log.debug("Request to get Compras : {}", id);
        return comprasRepository.findById(id).map(comprasMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compras : {}", id);
        comprasRepository.deleteById(id);
    }
}
