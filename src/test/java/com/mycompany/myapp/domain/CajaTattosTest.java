package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaTattosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CajaTattos.class);
        CajaTattos cajaTattos1 = new CajaTattos();
        cajaTattos1.setId(1L);
        CajaTattos cajaTattos2 = new CajaTattos();
        cajaTattos2.setId(cajaTattos1.getId());
        assertThat(cajaTattos1).isEqualTo(cajaTattos2);
        cajaTattos2.setId(2L);
        assertThat(cajaTattos1).isNotEqualTo(cajaTattos2);
        cajaTattos1.setId(null);
        assertThat(cajaTattos1).isNotEqualTo(cajaTattos2);
    }
}
