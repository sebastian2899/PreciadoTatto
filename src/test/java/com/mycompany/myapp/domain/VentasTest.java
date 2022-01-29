package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ventas.class);
        Ventas ventas1 = new Ventas();
        ventas1.setId(1L);
        Ventas ventas2 = new Ventas();
        ventas2.setId(ventas1.getId());
        assertThat(ventas1).isEqualTo(ventas2);
        ventas2.setId(2L);
        assertThat(ventas1).isNotEqualTo(ventas2);
        ventas1.setId(null);
        assertThat(ventas1).isNotEqualTo(ventas2);
    }
}
