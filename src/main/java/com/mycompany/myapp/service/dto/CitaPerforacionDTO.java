package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CitaPerforacion} entity.
 */
public class CitaPerforacionDTO implements Serializable {

    private Long id;

    private Instant fechaCreacionInicial;

    private Instant fechaCreacion;

    private Instant fechaCita;

    private String hora;

    private String nombreCliente;

    private BigDecimal valorPerforacion;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private String estado;

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

    public Instant getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public BigDecimal getValorPerforacion() {
        return valorPerforacion;
    }

    public void setValorPerforacion(BigDecimal valorPerforacion) {
        this.valorPerforacion = valorPerforacion;
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
        if (!(o instanceof CitaPerforacionDTO)) {
            return false;
        }

        CitaPerforacionDTO citaPerforacionDTO = (CitaPerforacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaPerforacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public Instant getFechaCreacionInicial() {
        return fechaCreacionInicial;
    }

    public void setFechaCreacionInicial(Instant fechaCreacionIncial) {
        this.fechaCreacionInicial = fechaCreacionIncial;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaPerforacionDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaCita='" + getFechaCita() + "'" +
            ", hora='" + getHora() + "'" +
            ", nombreCliente='" + getNombreCliente() + "'" +
            ", valorPerforacion=" + getValorPerforacion() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", estado=" + getEstado() +
            "}";
    }
}
