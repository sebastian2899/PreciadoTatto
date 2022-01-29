package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComprasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComprasDTO.class);
        ComprasDTO comprasDTO1 = new ComprasDTO();
        comprasDTO1.setId(1L);
        ComprasDTO comprasDTO2 = new ComprasDTO();
        assertThat(comprasDTO1).isNotEqualTo(comprasDTO2);
        comprasDTO2.setId(comprasDTO1.getId());
        assertThat(comprasDTO1).isEqualTo(comprasDTO2);
        comprasDTO2.setId(2L);
        assertThat(comprasDTO1).isNotEqualTo(comprasDTO2);
        comprasDTO1.setId(null);
        assertThat(comprasDTO1).isNotEqualTo(comprasDTO2);
    }
}
