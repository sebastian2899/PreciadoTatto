package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CitaPerforacion.
 */
@Entity
@Table(name = "cita_perforacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CitaPerforacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "fecha_creacion_inicial")
    private Instant fechaCreacionInicial;

    @Column(name = "fecha_cita")
    private Instant fechaCita;

    @Column(name = "hora")
    private String hora;

    @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "valor_perforacion", precision = 21, scale = 2)
    private BigDecimal valorPerforacion;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "valor_deuda", precision = 21, scale = 2)
    private BigDecimal valorDeuda;

    @Column(name = "estado")
    private String estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Instant getFechaCreacionInicial() {
        return fechaCreacionInicial;
    }

    public void setFechaCreacionInicial(Instant fechaCreacionInicial) {
        this.fechaCreacionInicial = fechaCreacionInicial;
    }

    public Long getId() {
        return this.id;
    }

    public CitaPerforacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public CitaPerforacion fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaCita() {
        return this.fechaCita;
    }

    public CitaPerforacion fechaCita(Instant fechaCita) {
        this.setFechaCita(fechaCita);
        return this;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHora() {
        return this.hora;
    }

    public CitaPerforacion hora(String hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public CitaPerforacion nombreCliente(String nombreCliente) {
        this.setNombreCliente(nombreCliente);
        return this;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public BigDecimal getValorPerforacion() {
        return this.valorPerforacion;
    }

    public CitaPerforacion valorPerforacion(BigDecimal valorPerforacion) {
        this.setValorPerforacion(valorPerforacion);
        return this;
    }

    public void setValorPerforacion(BigDecimal valorPerforacion) {
        this.valorPerforacion = valorPerforacion;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public CitaPerforacion valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public CitaPerforacion valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        if (!(o instanceof CitaPerforacion)) {
            return false;
        }
        return id != null && id.equals(((CitaPerforacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaPerforacion{" +
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

    public CitaPerforacion estado(String defaultEstado) {
        // TODO Auto-generated method stub
        return null;
    }
}
