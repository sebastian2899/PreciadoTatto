package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CajaIngresos.
 */
@Entity
@Table(name = "caja_ingresos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CajaIngresos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor_vendido_dia", precision = 21, scale = 2)
    private BigDecimal valorVendidoDia;

    @Column(name = "valor_registrado_dia", precision = 21, scale = 2)
    private BigDecimal valorRegistradoDia;

    @Column(name = "diferencia", precision = 21, scale = 2)
    private BigDecimal diferencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CajaIngresos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorVendidoDia() {
        return this.valorVendidoDia;
    }

    public CajaIngresos valorVendidoDia(BigDecimal valorVendidoDia) {
        this.setValorVendidoDia(valorVendidoDia);
        return this;
    }

    public void setValorVendidoDia(BigDecimal valorVendidoDia) {
        this.valorVendidoDia = valorVendidoDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return this.valorRegistradoDia;
    }

    public CajaIngresos valorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.setValorRegistradoDia(valorRegistradoDia);
        return this;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public BigDecimal getDiferencia() {
        return this.diferencia;
    }

    public CajaIngresos diferencia(BigDecimal diferencia) {
        this.setDiferencia(diferencia);
        return this;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CajaIngresos)) {
            return false;
        }
        return id != null && id.equals(((CajaIngresos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaIngresos{" +
            "id=" + getId() +
            ", valorVendidoDia=" + getValorVendidoDia() +
            ", valorRegistradoDia=" + getValorRegistradoDia() +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
