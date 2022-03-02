package com.mycompany.myapp.service.impl;

import com.itextpdf.text.pdf.events.IndexEvents.Entry;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.GaleriaFotos;
import com.mycompany.myapp.repository.GaleriaFotosRepository;
import com.mycompany.myapp.service.GaleriaFotosService;
import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
import com.mycompany.myapp.service.mapper.GaleriaFotosMapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    EntityManager entityManager;

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
    @Transactional
    public List<GaleriaFotosDTO> galeriaPorFiltro(GaleriaFotosDTO galeriaFotos) {
        log.debug("Request to get pictures per filtres");
        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        sb.append(Constants.FOTO_DISENIO_BASE);

        if (galeriaFotos.getNombreDisenio() != null && !galeriaFotos.getNombreDisenio().isEmpty()) {
            sb.append(Constants.FOTO_DISENIO_NOMBRE);
            filtros.put("nombre", "%" + galeriaFotos.getNombreDisenio().toUpperCase() + "%");
        }

        if (galeriaFotos.getPrecioDisenio() != null) {
            int resultado = galeriaFotos.getPrecioDisenio().compareTo(BigDecimal.ZERO);
            if (resultado != 0) {
                sb.append(Constants.FOTO_DISENIO_PRECIO);
                filtros.put("precio", galeriaFotos.getPrecioDisenio());
            }
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<GaleriaFotos> galFotos = q.getResultList();

        // return
        // galFotos.stream().map(galeriaFotosMapper::toDto).collect(Collectors.toCollection(LinkedList
        // :: new));
        return galeriaFotosMapper.toDto(galFotos);
    }

    @Override
    @Transactional
    public List<GaleriaFotosDTO> galeriaFotosOrder(String tipoOrden) {
        log.debug("Request to get picter per selection");

        List<GaleriaFotos> galeriaFotos = null;

        if (tipoOrden != null && !tipoOrden.isEmpty()) {
            if (tipoOrden.equalsIgnoreCase("Por Defecto")) {
                Query q = entityManager.createQuery(Constants.FOTO_DISENIO_BASE);
                galeriaFotos = q.getResultList();
            } else if (tipoOrden.equalsIgnoreCase("Mayor precio")) {
                Query q = entityManager.createQuery(Constants.FOTO_DINSENIO_MAYOR_PRECIO);
                galeriaFotos = q.getResultList();
            } else if (tipoOrden.equalsIgnoreCase("Menor precio")) {
                Query q = entityManager.createQuery(Constants.FOTO_DISENIO_MENOR_PRECIO);
                galeriaFotos = q.getResultList();
            }
        }

        return galeriaFotos.stream().map(galeriaFotosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
