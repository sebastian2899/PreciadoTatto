package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GaleriaFotos.
 */
@Entity
@Table(name = "galeria_fotos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GaleriaFotos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_disenio")
    private String nombreDisenio;

    @Lob
    @Column(name = "disenio")
    private byte[] disenio;

    @Column(name = "disenio_content_type")
    private String disenioContentType;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_disenio", precision = 21, scale = 2)
    private BigDecimal precioDisenio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GaleriaFotos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDisenio() {
        return this.nombreDisenio;
    }

    public GaleriaFotos nombreDisenio(String nombreDisenio) {
        this.setNombreDisenio(nombreDisenio);
        return this;
    }

    public void setNombreDisenio(String nombreDisenio) {
        this.nombreDisenio = nombreDisenio;
    }

    public byte[] getDisenio() {
        return this.disenio;
    }

    public GaleriaFotos disenio(byte[] disenio) {
        this.setDisenio(disenio);
        return this;
    }

    public void setDisenio(byte[] disenio) {
        this.disenio = disenio;
    }

    public String getDisenioContentType() {
        return this.disenioContentType;
    }

    public GaleriaFotos disenioContentType(String disenioContentType) {
        this.disenioContentType = disenioContentType;
        return this;
    }

    public void setDisenioContentType(String disenioContentType) {
        this.disenioContentType = disenioContentType;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public GaleriaFotos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioDisenio() {
        return this.precioDisenio;
    }

    public GaleriaFotos precioDisenio(BigDecimal precioDisenio) {
        this.setPrecioDisenio(precioDisenio);
        return this;
    }

    public void setPrecioDisenio(BigDecimal precioDisenio) {
        this.precioDisenio = precioDisenio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GaleriaFotos)) {
            return false;
        }
        return id != null && id.equals(((GaleriaFotos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GaleriaFotos{" +
            "id=" + getId() +
            ", nombreDisenio='" + getNombreDisenio() + "'" +
            ", disenio='" + getDisenio() + "'" +
            ", disenioContentType='" + getDisenioContentType() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioDisenio=" + getPrecioDisenio() +
            "}";
    }
}
