package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlarmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alarma.class);
        Alarma alarma1 = new Alarma();
        alarma1.setId(1L);
        Alarma alarma2 = new Alarma();
        alarma2.setId(alarma1.getId());
        assertThat(alarma1).isEqualTo(alarma2);
        alarma2.setId(2L);
        assertThat(alarma1).isNotEqualTo(alarma2);
        alarma1.setId(null);
        assertThat(alarma1).isNotEqualTo(alarma2);
    }
}
