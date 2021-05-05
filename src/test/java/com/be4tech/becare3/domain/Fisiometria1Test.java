package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Fisiometria1Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fisiometria1.class);
        Fisiometria1 fisiometria11 = new Fisiometria1();
        fisiometria11.setId(1L);
        Fisiometria1 fisiometria12 = new Fisiometria1();
        fisiometria12.setId(fisiometria11.getId());
        assertThat(fisiometria11).isEqualTo(fisiometria12);
        fisiometria12.setId(2L);
        assertThat(fisiometria11).isNotEqualTo(fisiometria12);
        fisiometria11.setId(null);
        assertThat(fisiometria11).isNotEqualTo(fisiometria12);
    }
}
