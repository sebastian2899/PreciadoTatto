package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTattoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitaTattoDTO.class);
        CitaTattoDTO citaTattoDTO1 = new CitaTattoDTO();
        citaTattoDTO1.setId(1L);
        CitaTattoDTO citaTattoDTO2 = new CitaTattoDTO();
        assertThat(citaTattoDTO1).isNotEqualTo(citaTattoDTO2);
        citaTattoDTO2.setId(citaTattoDTO1.getId());
        assertThat(citaTattoDTO1).isEqualTo(citaTattoDTO2);
        citaTattoDTO2.setId(2L);
        assertThat(citaTattoDTO1).isNotEqualTo(citaTattoDTO2);
        citaTattoDTO1.setId(null);
        assertThat(citaTattoDTO1).isNotEqualTo(citaTattoDTO2);
    }
}
