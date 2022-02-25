package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GaleriaFotosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GaleriaFotosDTO.class);
        GaleriaFotosDTO galeriaFotosDTO1 = new GaleriaFotosDTO();
        galeriaFotosDTO1.setId(1L);
        GaleriaFotosDTO galeriaFotosDTO2 = new GaleriaFotosDTO();
        assertThat(galeriaFotosDTO1).isNotEqualTo(galeriaFotosDTO2);
        galeriaFotosDTO2.setId(galeriaFotosDTO1.getId());
        assertThat(galeriaFotosDTO1).isEqualTo(galeriaFotosDTO2);
        galeriaFotosDTO2.setId(2L);
        assertThat(galeriaFotosDTO1).isNotEqualTo(galeriaFotosDTO2);
        galeriaFotosDTO1.setId(null);
        assertThat(galeriaFotosDTO1).isNotEqualTo(galeriaFotosDTO2);
    }
}
