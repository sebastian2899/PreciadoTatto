package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "productos_comprados")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductosComprados implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "id_Compra")
    private Long idCompra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, fechaCreacion, id, idCompra, productoId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductosComprados)) return false;
        ProductosComprados other = (ProductosComprados) obj;
        return (
            Objects.equals(cantidad, other.cantidad) &&
            Objects.equals(fechaCreacion, other.fechaCreacion) &&
            Objects.equals(id, other.id) &&
            Objects.equals(idCompra, other.idCompra) &&
            Objects.equals(productoId, other.productoId)
        );
    }

    @Override
    public String toString() {
        return (
            "ProductosComprados [id=" +
            id +
            ", fechaCreacion=" +
            fechaCreacion +
            ", productoId=" +
            productoId +
            ", cantidad=" +
            cantidad +
            ", idCompra=" +
            idCompra +
            "]"
        );
    }
}
