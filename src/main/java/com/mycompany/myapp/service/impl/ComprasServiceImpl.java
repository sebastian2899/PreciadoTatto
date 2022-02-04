package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Compras;
import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.ProductosComprados;
import com.mycompany.myapp.repository.ComprasRepository;
import com.mycompany.myapp.repository.ProductosCompradosRepository;
import com.mycompany.myapp.service.ComprasService;
import com.mycompany.myapp.service.dto.ComprasDTO;
import com.mycompany.myapp.service.dto.ProductoDTO;
import com.mycompany.myapp.service.mapper.ComprasMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 * Service Implementation for managing {@link Compras}.
 */
@Service
@Transactional
public class ComprasServiceImpl implements ComprasService {

    private final Logger log = LoggerFactory.getLogger(ComprasServiceImpl.class);

    private final ComprasRepository comprasRepository;

    private final ProductosCompradosRepository productosCompradosRepository;

    private final ComprasMapper comprasMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ComprasServiceImpl(
        ComprasRepository comprasRepository,
        ComprasMapper comprasMapper,
        ProductosCompradosRepository productosCompradosRepository
    ) {
        this.comprasRepository = comprasRepository;
        this.comprasMapper = comprasMapper;
        this.productosCompradosRepository = productosCompradosRepository;
    }

    @Override
    public ComprasDTO save(ComprasDTO comprasDTO) {
        log.debug("Request to save Compras : {}", comprasDTO);
        Compras compras = comprasMapper.toEntity(comprasDTO);
        compras = comprasRepository.save(compras);

        ProductosComprados productosComprados = null;

        if (comprasDTO.getProductosSeleccionados() != null) {
            for (ProductoDTO dto : comprasDTO.getProductosSeleccionados()) {
                productosComprados = new ProductosComprados();
                productosComprados.setFechaCreacion(Instant.now());
                productosComprados.setCantidad(dto.getCantidad());
                productosComprados.setIdCompra(comprasDTO.getId());
                productosComprados.setProductoId(dto.getId());

                productosCompradosRepository.save(productosComprados);

                Query q = entityManager
                    .createQuery("UPDATE Producto SET cantidad = cantidad+:cantidad WHERE id=:id")
                    .setParameter("cantidad", dto.getCantidad())
                    .setParameter("id", dto.getId());
                q.executeUpdate();
            }
        }

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
    public ComprasDTO findOne(Long id) {
        log.debug("Request to get Compras : {}", id);
        Compras compra = comprasRepository.compraPorId(id);
        compra.setProductosSeleccionados(consultarProductosVendidos(id));

        return comprasMapper.toDto(compra);
    }

    private List<ProductoDTO> consultarProductosVendidos(Long id) {
        List<ProductosComprados> productosComprados = productosCompradosRepository.productosComradosPorCompra(id);
        List<ProductoDTO> productos = new ArrayList<ProductoDTO>();

        ProductoDTO dto = null;
        Producto producto = null;

        for (ProductosComprados comprados : productosComprados) {
            dto = new ProductoDTO();
            dto.setId(comprados.getProductoId());
            dto.setCantidad(comprados.getCantidad());
            producto = productosCompradosRepository.productoPorId(comprados.getProductoId());
            dto.setNombre(producto.getNombre());
            dto.setPrecio(producto.getPrecio().multiply(new BigDecimal(dto.getCantidad())));

            productos.add(dto);
        }

        return productos;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compras : {}", id);
        comprasRepository.deleteById(id);
    }
}
