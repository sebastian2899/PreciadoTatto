package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CitaTatto} entity.
 */
public class CitaTattoDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private Instant fechaCita;

    private String hora;

    private String emailCliente;

    private String infoCliente;

    @Lob
    private byte[] fotoDiseno;

    private String fotoDisenoContentType;
    private BigDecimal valorTatto;

    private BigDecimal valorPagado;

    private BigDecimal deuda;

    private String estado;

    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoCliente() {
        return infoCliente;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
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

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public byte[] getFotoDiseno() {
        return fotoDiseno;
    }

    public void setFotoDiseno(byte[] fotoDiseno) {
        this.fotoDiseno = fotoDiseno;
    }

    public String getFotoDisenoContentType() {
        return fotoDisenoContentType;
    }

    public void setFotoDisenoContentType(String fotoDisenoContentType) {
        this.fotoDisenoContentType = fotoDisenoContentType;
    }

    public BigDecimal getValorTatto() {
        return valorTatto;
    }

    public void setValorTatto(BigDecimal valorTatto) {
        this.valorTatto = valorTatto;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getDeuda() {
        return deuda;
    }

    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaTattoDTO)) {
            return false;
        }

        CitaTattoDTO citaTattoDTO = (CitaTattoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaTattoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaTattoDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaCita='" + getFechaCita() + "'" +
            ", hora='" + getHora() + "'" +
            ", emailCliente='" + getEmailCliente() + "'" +
            ", fotoDiseno='" + getFotoDiseno() + "'" +
            ", valorTatto=" + getValorTatto() +
            ", valorPagado=" + getValorPagado() +
            ", deuda=" + getDeuda() +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
