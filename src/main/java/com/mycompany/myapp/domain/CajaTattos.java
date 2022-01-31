package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CajaTattos.
 */
@Entity
@Table(name = "caja_tattos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CajaTattos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor_tatto_dia", precision = 21, scale = 2)
    private BigDecimal valorTattoDia;

    @Column(name = "valor_registrado", precision = 21, scale = 2)
    private BigDecimal valorRegistrado;

    @Column(name = "diferencia", precision = 21, scale = 2)
    private BigDecimal diferencia;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return this.id;
    }

    public CajaTattos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTattoDia() {
        return this.valorTattoDia;
    }

    public CajaTattos valorTattoDia(BigDecimal valorTattoDia) {
        this.setValorTattoDia(valorTattoDia);
        return this;
    }

    public void setValorTattoDia(BigDecimal valorTattoDia) {
        this.valorTattoDia = valorTattoDia;
    }

    public BigDecimal getValorRegistrado() {
        return this.valorRegistrado;
    }

    public CajaTattos valorRegistrado(BigDecimal valorRegistrado) {
        this.setValorRegistrado(valorRegistrado);
        return this;
    }

    public void setValorRegistrado(BigDecimal valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
    }

    public BigDecimal getDiferencia() {
        return this.diferencia;
    }

    public CajaTattos diferencia(BigDecimal diferencia) {
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
        if (!(o instanceof CajaTattos)) {
            return false;
        }
        return id != null && id.equals(((CajaTattos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaTattos{" +
            "id=" + getId() +
            ", valorTattoDia=" + getValorTattoDia() +
            ", valorRegistrado=" + getValorRegistrado() +
            ", diferencia=" + getDiferencia() +
            "}";
    }
}
