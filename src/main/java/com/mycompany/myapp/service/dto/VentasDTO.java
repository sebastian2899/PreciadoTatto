package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Ventas} entity.
 */
public class VentasDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private Long idCliente;

    private BigDecimal valorVenta;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private String estado;

    private List<ProductoDTO> productosSeleccionados;

    public Long getId() {
        return id;
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
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(BigDecimal valorVenta) {
        this.valorVenta = valorVenta;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentasDTO)) {
            return false;
        }

        VentasDTO ventasDTO = (VentasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentasDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", idCliente=" + getIdCliente() +
            ", valorVenta=" + getValorVenta() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
