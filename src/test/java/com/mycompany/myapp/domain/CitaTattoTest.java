package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTattoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitaTatto.class);
        CitaTatto citaTatto1 = new CitaTatto();
        citaTatto1.setId(1L);
        CitaTatto citaTatto2 = new CitaTatto();
        citaTatto2.setId(citaTatto1.getId());
        assertThat(citaTatto1).isEqualTo(citaTatto2);
        citaTatto2.setId(2L);
        assertThat(citaTatto1).isNotEqualTo(citaTatto2);
        citaTatto1.setId(null);
        assertThat(citaTatto1).isNotEqualTo(citaTatto2);
    }
}
