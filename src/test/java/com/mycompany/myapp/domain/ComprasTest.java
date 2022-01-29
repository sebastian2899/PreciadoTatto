package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComprasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compras.class);
        Compras compras1 = new Compras();
        compras1.setId(1L);
        Compras compras2 = new Compras();
        compras2.setId(compras1.getId());
        assertThat(compras1).isEqualTo(compras2);
        compras2.setId(2L);
        assertThat(compras1).isNotEqualTo(compras2);
        compras1.setId(null);
        assertThat(compras1).isNotEqualTo(compras2);
    }
}
