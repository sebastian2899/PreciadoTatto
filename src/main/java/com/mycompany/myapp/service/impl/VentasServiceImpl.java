package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.ProductosVendidos;
import com.mycompany.myapp.domain.Ventas;
import com.mycompany.myapp.repository.ProductosVendidosRepository;
import com.mycompany.myapp.repository.VentasRepository;
import com.mycompany.myapp.service.VentasService;
import com.mycompany.myapp.service.dto.ProductoDTO;
import com.mycompany.myapp.service.dto.VentasDTO;
import com.mycompany.myapp.service.mapper.VentasMapper;
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
 * Service Implementation for managing {@link Ventas}.
 */
@Service
@Transactional
public class VentasServiceImpl implements VentasService {

    private final Logger log = LoggerFactory.getLogger(VentasServiceImpl.class);

    private final VentasRepository ventasRepository;

    private final ProductosVendidosRepository productosVendidosRepository;

    private final VentasMapper ventasMapper;

    public VentasServiceImpl(
        VentasRepository ventasRepository,
        VentasMapper ventasMapper,
        ProductosVendidosRepository productosVendidosRepository
    ) {
        this.ventasRepository = ventasRepository;
        this.ventasMapper = ventasMapper;
        this.productosVendidosRepository = productosVendidosRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VentasDTO save(VentasDTO ventasDTO) {
        log.debug("Request to save Ventas : {}", ventasDTO);
        Ventas ventas = ventasMapper.toEntity(ventasDTO);
        ventas.setFechaCreacion(Instant.now());
        ventas = ventasRepository.save(ventas);

        ProductosVendidos productosVendidos = null;

        if (ventasDTO.getProductosSeleccionados() != null) {
            for (ProductoDTO prod : ventasDTO.getProductosSeleccionados()) {
                productosVendidos = new ProductosVendidos();
                productosVendidos.setFechaCreacion(Instant.now());
                productosVendidos.setCantidad(prod.getCantidad());
                productosVendidos.setIdVenta(ventas.getId());
                productosVendidos.setProductoId(prod.getId());

                productosVendidosRepository.save(productosVendidos);
                //Eliminamos los productos que se vendieron

                Query q = entityManager
                    .createQuery("UPDATE Producto SET cantidad = cantidad-:cantidad WHERE id=:id")
                    .setParameter("cantidad", prod.getCantidad())
                    .setParameter("id", prod.getId());
                q.executeUpdate();
            }
        }

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

    private List<ProductoDTO> consultarProductosVendidos(Long id) {
        List<ProductosVendidos> productosVendidos = productosVendidosRepository.productosVendidosPorVenta(id);
        List<ProductoDTO> productos = new ArrayList<ProductoDTO>();
        Producto prod = null;
        ProductoDTO producto = null;

        for (ProductosVendidos vendidos : productosVendidos) {
            producto = new ProductoDTO();
            producto.setCantidad(vendidos.getCantidad());
            prod = productosVendidosRepository.productoByID(vendidos.getProductoId());
            producto.setNombre(prod.getNombre());
            producto.setPrecio(prod.getPrecio().multiply(new BigDecimal(producto.getCantidad())));

            productos.add(producto);
        }

        return productos;
    }

    @Override
    @Transactional(readOnly = true)
    public VentasDTO findOne(Long id) {
        log.debug("Request to get Ventas : {}", id);

        Ventas venta = ventasRepository.ventaPorId(id);
        venta.setProductosSeleccionados(consultarProductosVendidos(id));

        return ventasMapper.toDto(venta);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ventas : {}", id);
        ventasRepository.deleteById(id);
    }
}
