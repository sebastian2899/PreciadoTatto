package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Abono.
 */
@Entity
@Table(name = "abono")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Abono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_abono")
    private Instant fechaAbono;

    @Column(name = "id_cita")
    private Long idCita;

    @Column(name = "valor_abono", precision = 21, scale = 2)
    private BigDecimal valorAbono;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Abono id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaAbono() {
        return this.fechaAbono;
    }

    public Abono fechaAbono(Instant fechaAbono) {
        this.setFechaAbono(fechaAbono);
        return this;
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
        return this.valorAbono;
    }

    public Abono valorAbono(BigDecimal valorAbono) {
        this.setValorAbono(valorAbono);
        return this;
    }

    public void setValorAbono(BigDecimal valorAbono) {
        this.valorAbono = valorAbono;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abono)) {
            return false;
        }
        return id != null && id.equals(((Abono) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abono{" +
            "id=" + getId() +
            ", fechaAbono='" + getFechaAbono() + "'" +
            ", idCita=" + getIdCita() +
            ", valorAbono=" + getValorAbono() +
            "}";
    }

    public Abono idCita(Long updatedIdFactura) {
        // TODO Auto-generated method stub
        return null;
    }
}
