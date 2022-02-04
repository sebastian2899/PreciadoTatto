package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Abono;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.AbonoRepository;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.AbonoService;
import com.mycompany.myapp.service.dto.AbonoDTO;
import com.mycompany.myapp.service.mapper.AbonoMapper;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Abono}.
 */
@Service
@Transactional
public class AbonoServiceImpl implements AbonoService {

    private final Logger log = LoggerFactory.getLogger(AbonoServiceImpl.class);

    private final AbonoRepository abonoRepository;

    private final CitaTattoRepository citaTattoRepository;

    private final AbonoMapper abonoMapper;

    public AbonoServiceImpl(AbonoRepository abonoRepository, AbonoMapper abonoMapper, CitaTattoRepository citaTattoRepository) {
        this.abonoRepository = abonoRepository;
        this.abonoMapper = abonoMapper;
        this.citaTattoRepository = citaTattoRepository;
    }

    @Override
    public AbonoDTO save(AbonoDTO abonoDTO) {
        log.debug("Request to save Abono : {}", abonoDTO);
        Abono abono = abonoMapper.toEntity(abonoDTO);

        if (abonoDTO.getIdCita() != null) {
            CitaTatto cita = citaTattoRepository.citaById(abonoDTO.getIdCita());
            cita.setDeuda(cita.getDeuda().subtract(abonoDTO.getValorAbono()));

            citaTattoRepository.save(cita);
            abono.setIdCita(cita.getId());
        }

        abono = abonoRepository.save(abono);
        return abonoMapper.toDto(abono);
    }

    @Override
    public Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO) {
        log.debug("Request to partially update Abono : {}", abonoDTO);

        return abonoRepository
            .findById(abonoDTO.getId())
            .map(existingAbono -> {
                abonoMapper.partialUpdate(existingAbono, abonoDTO);

                return existingAbono;
            })
            .map(abonoRepository::save)
            .map(abonoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbonoDTO> findAll() {
        log.debug("Request to get all Abonos");
        return abonoRepository.findAll().stream().map(abonoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AbonoDTO> findOne(Long id) {
        log.debug("Request to get Abono : {}", id);
        return abonoRepository.findById(id).map(abonoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abono : {}", id);
        abonoRepository.deleteById(id);
    }

    @Override
    public BigDecimal valorDeuda(Long id) {
        BigDecimal deuda = abonoRepository.valorDeuda(id);

        if (deuda == null) {
            deuda = BigDecimal.ZERO;
        }

        return deuda;
    }

    @Override
    public List<AbonoDTO> consultarAbonoPorCita(Long idCita) {
        return abonoRepository.abonosPorCita(idCita).stream().map(abonoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
