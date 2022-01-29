package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaIngresosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CajaIngresosDTO.class);
        CajaIngresosDTO cajaIngresosDTO1 = new CajaIngresosDTO();
        cajaIngresosDTO1.setId(1L);
        CajaIngresosDTO cajaIngresosDTO2 = new CajaIngresosDTO();
        assertThat(cajaIngresosDTO1).isNotEqualTo(cajaIngresosDTO2);
        cajaIngresosDTO2.setId(cajaIngresosDTO1.getId());
        assertThat(cajaIngresosDTO1).isEqualTo(cajaIngresosDTO2);
        cajaIngresosDTO2.setId(2L);
        assertThat(cajaIngresosDTO1).isNotEqualTo(cajaIngresosDTO2);
        cajaIngresosDTO1.setId(null);
        assertThat(cajaIngresosDTO1).isNotEqualTo(cajaIngresosDTO2);
    }
}
