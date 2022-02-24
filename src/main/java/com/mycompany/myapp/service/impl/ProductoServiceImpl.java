package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.repository.ProductoRepository;
import com.mycompany.myapp.service.ProductoService;
import com.mycompany.myapp.service.dto.ProductoDTO;
import com.mycompany.myapp.service.mapper.ProductoMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);
        producto = productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO) {
        log.debug("Request to partially update Producto : {}", productoDTO);

        return productoRepository
            .findById(productoDTO.getId())
            .map(existingProducto -> {
                productoMapper.partialUpdate(existingProducto, productoDTO);

                return existingProducto;
            })
            .map(productoRepository::save)
            .map(productoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        log.debug("Request to get all Productos");
        return productoRepository.findAll().stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id).map(productoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }

    @Override
    public Long totalProductos() {
        log.debug("Request to get count");

        Query q = entityManager.createQuery(Constants.TOTAL_PRODUCTOS);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long ventasHoy() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        Query q = entityManager.createQuery(Constants.TOTAL_VENTAS_HOY).setParameter("fecha", fecha);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long comprasHoy() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());

        Query q = entityManager.createQuery(Constants.TOTAL_COMPRAS_HOY).setParameter("fecha", fecha);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<ProductoDTO> productosPorFiltro(ProductoDTO producto) {
        log.debug("Request to get products for filters");
        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<>();

        sb.append(Constants.PRODUCTO_BASE);
        if (producto.getNombre() != null && !producto.getNombre().isEmpty()) {
            sb.append(Constants.PRODUCTO_NOMBRE);
            filtros.put("nombre", "%" + producto.getNombre().toUpperCase() + "%");
        }

        Query q = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List<Producto> productos = q.getResultList();

        return productoMapper.toDto(productos);
    }

    @Override
    public List<ProductoDTO> aviableProducts() {
        log.debug("Request to get all aviable Products");
        List<Producto> productos = productoRepository.productosDisponibles();
        return productoMapper.toDto(productos);
    }

    @Override
    public boolean productosPorVentas(Long id) {
        log.debug("Request to get sold productos");
        boolean resp = true;

        Query q = entityManager.createQuery(Constants.CONSULTAR_PRODUCTOS_VENTAS).setParameter("productoId", id);

        Long productosVentas = (Long) q.getSingleResult();

        if (productosVentas == null || productosVentas == 0) {
            resp = false;
        }

        return resp;
    }

    @Override
    public List<ProductoDTO> productosAgotados() {
        log.debug("Request to get all not aviable productos");

        Query q = entityManager.createQuery(Constants.PRODUCTOS_AGOTADOS);

        List<Producto> produtosAgotados = q.getResultList();

        return productoMapper.toDto(produtosAgotados);
    }
}
