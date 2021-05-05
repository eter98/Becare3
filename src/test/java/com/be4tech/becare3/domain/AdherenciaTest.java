package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdherenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adherencia.class);
        Adherencia adherencia1 = new Adherencia();
        adherencia1.setId(1L);
        Adherencia adherencia2 = new Adherencia();
        adherencia2.setId(adherencia1.getId());
        assertThat(adherencia1).isEqualTo(adherencia2);
        adherencia2.setId(2L);
        assertThat(adherencia1).isNotEqualTo(adherencia2);
        adherencia1.setId(null);
        assertThat(adherencia1).isNotEqualTo(adherencia2);
    }
}
