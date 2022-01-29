package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CajaTattos} entity.
 */
public class CajaTattosDTO implements Serializable {

    private Long id;

    private BigDecimal valorTattoDia;

    private BigDecimal valorRegistrado;

    private BigDecimal diferencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTattoDia() {
        return valorTattoDia;
    }

    public void setValorTattoDia(BigDecimal valorTattoDia) {
        this.valorTattoDia = valorTattoDia;
    }

    public BigDecimal getValorRegistrado() {
        return valorRegistrado;
    }

    public void setValorRegistrado(BigDecimal valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
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
        if (!(o instanceof CajaTattosDTO)) {
            return false;
        }

        CajaTattosDTO cajaTattosDTO = (CajaTattosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaTattosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaTattosDTO{" +
            "id=" + getId() +
            ", valorTattoDia=" + getValorTattoDia() +
            ", valorRegistrado=" + getValorRegistrado() +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
