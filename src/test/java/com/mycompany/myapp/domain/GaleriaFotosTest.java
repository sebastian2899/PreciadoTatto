package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GaleriaFotosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GaleriaFotos.class);
        GaleriaFotos galeriaFotos1 = new GaleriaFotos();
        galeriaFotos1.setId(1L);
        GaleriaFotos galeriaFotos2 = new GaleriaFotos();
        galeriaFotos2.setId(galeriaFotos1.getId());
        assertThat(galeriaFotos1).isEqualTo(galeriaFotos2);
        galeriaFotos2.setId(2L);
        assertThat(galeriaFotos1).isNotEqualTo(galeriaFotos2);
        galeriaFotos1.setId(null);
        assertThat(galeriaFotos1).isNotEqualTo(galeriaFotos2);
    }
}
