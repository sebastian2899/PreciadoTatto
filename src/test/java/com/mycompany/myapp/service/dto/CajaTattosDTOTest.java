package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaTattosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CajaTattosDTO.class);
        CajaTattosDTO cajaTattosDTO1 = new CajaTattosDTO();
        cajaTattosDTO1.setId(1L);
        CajaTattosDTO cajaTattosDTO2 = new CajaTattosDTO();
        assertThat(cajaTattosDTO1).isNotEqualTo(cajaTattosDTO2);
        cajaTattosDTO2.setId(cajaTattosDTO1.getId());
        assertThat(cajaTattosDTO1).isEqualTo(cajaTattosDTO2);
        cajaTattosDTO2.setId(2L);
        assertThat(cajaTattosDTO1).isNotEqualTo(cajaTattosDTO2);
        cajaTattosDTO1.setId(null);
        assertThat(cajaTattosDTO1).isNotEqualTo(cajaTattosDTO2);
    }
}
