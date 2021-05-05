package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pasos.class);
        Pasos pasos1 = new Pasos();
        pasos1.setId(1L);
        Pasos pasos2 = new Pasos();
        pasos2.setId(pasos1.getId());
        assertThat(pasos1).isEqualTo(pasos2);
        pasos2.setId(2L);
        assertThat(pasos1).isNotEqualTo(pasos2);
        pasos1.setId(null);
        assertThat(pasos1).isNotEqualTo(pasos2);
    }
}
