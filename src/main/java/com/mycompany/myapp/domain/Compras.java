package com.mycompany.myapp.domain;

import com.mycompany.myapp.service.dto.ProductoDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compras.
 */
@Entity
@Table(name = "compras")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compras implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "valor_compra", precision = 21, scale = 2)
    private BigDecimal valorCompra;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "valor_deuda", precision = 21, scale = 2)
    private BigDecimal valorDeuda;

    @Column(name = "estado")
    private String estado;

    @Transient
    private List<ProductoDTO> productosSeleccionados;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compras id(Long id) {
        this.setId(id);
        return this;
    }

    public List<ProductoDTO> getProductosSeleccionados() {
        return productosSeleccionados;
    }

    public void setProductosSeleccionados(List<ProductoDTO> productosSeleccionados) {
        this.productosSeleccionados = productosSeleccionados;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Compras fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorCompra() {
        return this.valorCompra;
    }

    public Compras valorCompra(BigDecimal valorCompra) {
        this.setValorCompra(valorCompra);
        return this;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public Compras valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public Compras valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getEstado() {
        return this.estado;
    }

    public Compras estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compras)) {
            return false;
        }
        return id != null && id.equals(((Compras) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compras{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorCompra=" + getValorCompra() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
