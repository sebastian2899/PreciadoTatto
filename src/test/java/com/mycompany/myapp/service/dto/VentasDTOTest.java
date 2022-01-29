package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentasDTO.class);
        VentasDTO ventasDTO1 = new VentasDTO();
        ventasDTO1.setId(1L);
        VentasDTO ventasDTO2 = new VentasDTO();
        assertThat(ventasDTO1).isNotEqualTo(ventasDTO2);
        ventasDTO2.setId(ventasDTO1.getId());
        assertThat(ventasDTO1).isEqualTo(ventasDTO2);
        ventasDTO2.setId(2L);
        assertThat(ventasDTO1).isNotEqualTo(ventasDTO2);
        ventasDTO1.setId(null);
        assertThat(ventasDTO1).isNotEqualTo(ventasDTO2);
    }
}
