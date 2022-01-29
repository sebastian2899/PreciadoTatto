package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaIngresosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CajaIngresos.class);
        CajaIngresos cajaIngresos1 = new CajaIngresos();
        cajaIngresos1.setId(1L);
        CajaIngresos cajaIngresos2 = new CajaIngresos();
        cajaIngresos2.setId(cajaIngresos1.getId());
        assertThat(cajaIngresos1).isEqualTo(cajaIngresos2);
        cajaIngresos2.setId(2L);
        assertThat(cajaIngresos1).isNotEqualTo(cajaIngresos2);
        cajaIngresos1.setId(null);
        assertThat(cajaIngresos1).isNotEqualTo(cajaIngresos2);
    }
}
