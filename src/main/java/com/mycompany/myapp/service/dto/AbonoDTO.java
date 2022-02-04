package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Abono} entity.
 */
public class AbonoDTO implements Serializable {

    private Long id;

    private Instant fechaAbono;

    private Long idCita;

    private BigDecimal valorAbono;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(Instant fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public BigDecimal getValorAbono() {
        return valorAbono;
    }

    public void setValorAbono(BigDecimal valorAbono) {
        this.valorAbono = valorAbono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbonoDTO)) {
            return false;
        }

        AbonoDTO abonoDTO = (AbonoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, abonoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbonoDTO{" +
            "id=" + getId() +
            ", fechaAbono='" + getFechaAbono() + "'" +
            ", idFactura=" + getIdCita() +
            ", valorAbono=" + getValorAbono() +
            "}";
    }
}
