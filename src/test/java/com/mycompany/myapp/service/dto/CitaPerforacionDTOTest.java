package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaPerforacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitaPerforacionDTO.class);
        CitaPerforacionDTO citaPerforacionDTO1 = new CitaPerforacionDTO();
        citaPerforacionDTO1.setId(1L);
        CitaPerforacionDTO citaPerforacionDTO2 = new CitaPerforacionDTO();
        assertThat(citaPerforacionDTO1).isNotEqualTo(citaPerforacionDTO2);
        citaPerforacionDTO2.setId(citaPerforacionDTO1.getId());
        assertThat(citaPerforacionDTO1).isEqualTo(citaPerforacionDTO2);
        citaPerforacionDTO2.setId(2L);
        assertThat(citaPerforacionDTO1).isNotEqualTo(citaPerforacionDTO2);
        citaPerforacionDTO1.setId(null);
        assertThat(citaPerforacionDTO1).isNotEqualTo(citaPerforacionDTO2);
    }
}
