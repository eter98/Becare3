package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OximetriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Oximetria.class);
        Oximetria oximetria1 = new Oximetria();
        oximetria1.setId(1L);
        Oximetria oximetria2 = new Oximetria();
        oximetria2.setId(oximetria1.getId());
        assertThat(oximetria1).isEqualTo(oximetria2);
        oximetria2.setId(2L);
        assertThat(oximetria1).isNotEqualTo(oximetria2);
        oximetria1.setId(null);
        assertThat(oximetria1).isNotEqualTo(oximetria2);
    }
}
