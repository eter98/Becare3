package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrecuenciaCardiacaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrecuenciaCardiaca.class);
        FrecuenciaCardiaca frecuenciaCardiaca1 = new FrecuenciaCardiaca();
        frecuenciaCardiaca1.setId(1L);
        FrecuenciaCardiaca frecuenciaCardiaca2 = new FrecuenciaCardiaca();
        frecuenciaCardiaca2.setId(frecuenciaCardiaca1.getId());
        assertThat(frecuenciaCardiaca1).isEqualTo(frecuenciaCardiaca2);
        frecuenciaCardiaca2.setId(2L);
        assertThat(frecuenciaCardiaca1).isNotEqualTo(frecuenciaCardiaca2);
        frecuenciaCardiaca1.setId(null);
        assertThat(frecuenciaCardiaca1).isNotEqualTo(frecuenciaCardiaca2);
    }
}
