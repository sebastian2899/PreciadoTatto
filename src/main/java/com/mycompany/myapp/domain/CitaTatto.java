package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CitaTatto.
 */
@Entity
@Table(name = "cita_tatto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CitaTatto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "info_cliente")
    private String infoCliente;

    @Column(name = "fecha_cita")
    private Instant fechaCita;

    @Column(name = "hora")
    private String hora;

    @Column(name = "email_cliente")
    private String emailCliente;

    @Lob
    @Column(name = "foto_diseno")
    private byte[] fotoDiseno;

    @Column(name = "foto_diseno_content_type")
    private String fotoDisenoContentType;

    @Column(name = "valor_tatto", precision = 21, scale = 2)
    private BigDecimal valorTatto;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "deuda", precision = 21, scale = 2)
    private BigDecimal deuda;

    @Column(name = "estado")
    private String estado;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado_cita")
    private String estadoCita;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getInfoCliente() {
        return infoCliente;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public CitaTatto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public CitaTatto fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaCita() {
        return this.fechaCita;
    }

    public CitaTatto fechaCita(Instant fechaCita) {
        this.setFechaCita(fechaCita);
        return this;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHora() {
        return this.hora;
    }

    public CitaTatto hora(String hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEmailCliente() {
        return this.emailCliente;
    }

    public CitaTatto emailCliente(String emailCliente) {
        this.setEmailCliente(emailCliente);
        return this;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public byte[] getFotoDiseno() {
        return this.fotoDiseno;
    }

    public CitaTatto fotoDiseno(byte[] fotoDiseno) {
        this.setFotoDiseno(fotoDiseno);
        return this;
    }

    public void setFotoDiseno(byte[] fotoDiseno) {
        this.fotoDiseno = fotoDiseno;
    }

    public String getFotoDisenoContentType() {
        return this.fotoDisenoContentType;
    }

    public CitaTatto fotoDisenoContentType(String fotoDisenoContentType) {
        this.fotoDisenoContentType = fotoDisenoContentType;
        return this;
    }

    public void setFotoDisenoContentType(String fotoDisenoContentType) {
        this.fotoDisenoContentType = fotoDisenoContentType;
    }

    public BigDecimal getValorTatto() {
        return this.valorTatto;
    }

    public CitaTatto valorTatto(BigDecimal valorTatto) {
        this.setValorTatto(valorTatto);
        return this;
    }

    public void setValorTatto(BigDecimal valorTatto) {
        this.valorTatto = valorTatto;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public CitaTatto valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getDeuda() {
        return this.deuda;
    }

    public CitaTatto deuda(BigDecimal deuda) {
        this.setDeuda(deuda);
        return this;
    }

    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }

    public String getEstado() {
        return this.estado;
    }

    public CitaTatto estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public CitaTatto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaTatto)) {
            return false;
        }
        return id != null && id.equals(((CitaTatto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaTatto{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaCita='" + getFechaCita() + "'" +
            ", hora='" + getHora() + "'" +
            ", emailCliente='" + getEmailCliente() + "'" +
            ", fotoDiseno='" + getFotoDiseno() + "'" +
            ", fotoDisenoContentType='" + getFotoDisenoContentType() + "'" +
            ", valorTatto=" + getValorTatto() +
            ", valorPagado=" + getValorPagado() +
            ", deuda=" + getDeuda() +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
