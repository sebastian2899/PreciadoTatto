package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AbonoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbonoDTO.class);
        AbonoDTO abonoDTO1 = new AbonoDTO();
        abonoDTO1.setId(1L);
        AbonoDTO abonoDTO2 = new AbonoDTO();
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
        abonoDTO2.setId(abonoDTO1.getId());
        assertThat(abonoDTO1).isEqualTo(abonoDTO2);
        abonoDTO2.setId(2L);
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
        abonoDTO1.setId(null);
        assertThat(abonoDTO1).isNotEqualTo(abonoDTO2);
    }
}
