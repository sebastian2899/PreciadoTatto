package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbonoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Abono.class);
        Abono abono1 = new Abono();
        abono1.setId(1L);
        Abono abono2 = new Abono();
        abono2.setId(abono1.getId());
        assertThat(abono1).isEqualTo(abono2);
        abono2.setId(2L);
        assertThat(abono1).isNotEqualTo(abono2);
        abono1.setId(null);
        assertThat(abono1).isNotEqualTo(abono2);
    }
}
