package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemperaturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Temperatura.class);
        Temperatura temperatura1 = new Temperatura();
        temperatura1.setId(1L);
        Temperatura temperatura2 = new Temperatura();
        temperatura2.setId(temperatura1.getId());
        assertThat(temperatura1).isEqualTo(temperatura2);
        temperatura2.setId(2L);
        assertThat(temperatura1).isNotEqualTo(temperatura2);
        temperatura1.setId(null);
        assertThat(temperatura1).isNotEqualTo(temperatura2);
    }
}
