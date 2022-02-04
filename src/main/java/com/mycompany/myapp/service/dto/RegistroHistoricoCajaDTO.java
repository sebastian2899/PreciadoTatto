package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class RegistroHistoricoCajaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal valorVendido;

    private BigDecimal valorPagado;

    private BigDecimal diferencia;

    public BigDecimal getValorVendido() {
        return valorVendido;
    }

    public void setValorVendido(BigDecimal valorVendido) {
        this.valorVendido = valorVendido;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diferencia, valorPagado, valorVendido);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RegistroHistoricoCajaDTO)) return false;
        RegistroHistoricoCajaDTO other = (RegistroHistoricoCajaDTO) obj;
        return (
            Objects.equals(diferencia, other.diferencia) &&
            Objects.equals(valorPagado, other.valorPagado) &&
            Objects.equals(valorVendido, other.valorVendido)
        );
    }

    @Override
    public String toString() {
        return (
            "RegistroHistoricoCajaDTO [valorVendido=" + valorVendido + ", valorPagado=" + valorPagado + ", diferencia=" + diferencia + "]"
        );
    }
}
