package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaPerforacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitaPerforacion.class);
        CitaPerforacion citaPerforacion1 = new CitaPerforacion();
        citaPerforacion1.setId(1L);
        CitaPerforacion citaPerforacion2 = new CitaPerforacion();
        citaPerforacion2.setId(citaPerforacion1.getId());
        assertThat(citaPerforacion1).isEqualTo(citaPerforacion2);
        citaPerforacion2.setId(2L);
        assertThat(citaPerforacion1).isNotEqualTo(citaPerforacion2);
        citaPerforacion1.setId(null);
        assertThat(citaPerforacion1).isNotEqualTo(citaPerforacion2);
    }
}
