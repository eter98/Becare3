package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaloriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caloria.class);
        Caloria caloria1 = new Caloria();
        caloria1.setId(1L);
        Caloria caloria2 = new Caloria();
        caloria2.setId(caloria1.getId());
        assertThat(caloria1).isEqualTo(caloria2);
        caloria2.setId(2L);
        assertThat(caloria1).isNotEqualTo(caloria2);
        caloria1.setId(null);
        assertThat(caloria1).isNotEqualTo(caloria2);
    }
}
