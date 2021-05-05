package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PreguntaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pregunta.class);
        Pregunta pregunta1 = new Pregunta();
        pregunta1.setId(1L);
        Pregunta pregunta2 = new Pregunta();
        pregunta2.setId(pregunta1.getId());
        assertThat(pregunta1).isEqualTo(pregunta2);
        pregunta2.setId(2L);
        assertThat(pregunta1).isNotEqualTo(pregunta2);
        pregunta1.setId(null);
        assertThat(pregunta1).isNotEqualTo(pregunta2);
    }
}
