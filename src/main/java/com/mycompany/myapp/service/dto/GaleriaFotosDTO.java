package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.GaleriaFotos} entity.
 */
public class GaleriaFotosDTO implements Serializable {

    private Long id;

    private String nombreDisenio;

    @Lob
    private byte[] disenio;

    private String disenioContentType;
    private String descripcion;

    private BigDecimal precioDisenio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDisenio() {
        return nombreDisenio;
    }

    public void setNombreDisenio(String nombreDisenio) {
        this.nombreDisenio = nombreDisenio;
    }

    public byte[] getDisenio() {
        return disenio;
    }

    public void setDisenio(byte[] disenio) {
        this.disenio = disenio;
    }

    public String getDisenioContentType() {
        return disenioContentType;
    }

    public void setDisenioContentType(String disenioContentType) {
        this.disenioContentType = disenioContentType;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioDisenio() {
        return precioDisenio;
    }

    public void setPrecioDisenio(BigDecimal precioDisenio) {
        this.precioDisenio = precioDisenio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GaleriaFotosDTO)) {
            return false;
        }

        GaleriaFotosDTO galeriaFotosDTO = (GaleriaFotosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, galeriaFotosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GaleriaFotosDTO{" +
            "id=" + getId() +
            ", nombreDisenio='" + getNombreDisenio() + "'" +
            ", disenio='" + getDisenio() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioDisenio=" + getPrecioDisenio() +
            "}";
    }
}
