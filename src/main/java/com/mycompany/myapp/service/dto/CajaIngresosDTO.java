package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CajaIngresos} entity.
 */
public class CajaIngresosDTO implements Serializable {

    private Long id;

    private BigDecimal valorVendidoDia;

    private BigDecimal valorRegistradoDia;

    private BigDecimal diferencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorVendidoDia() {
        return valorVendidoDia;
    }

    public void setValorVendidoDia(BigDecimal valorVendidoDia) {
        this.valorVendidoDia = valorVendidoDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return valorRegistradoDia;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CajaIngresosDTO)) {
            return false;
        }

        CajaIngresosDTO cajaIngresosDTO = (CajaIngresosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaIngresosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaIngresosDTO{" +
            "id=" + getId() +
            ", valorVendidoDia=" + getValorVendidoDia() +
            ", valorRegistradoDia=" + getValorRegistradoDia() +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
