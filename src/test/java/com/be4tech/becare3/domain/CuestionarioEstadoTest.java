package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CuestionarioEstadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuestionarioEstado.class);
        CuestionarioEstado cuestionarioEstado1 = new CuestionarioEstado();
        cuestionarioEstado1.setId(1L);
        CuestionarioEstado cuestionarioEstado2 = new CuestionarioEstado();
        cuestionarioEstado2.setId(cuestionarioEstado1.getId());
        assertThat(cuestionarioEstado1).isEqualTo(cuestionarioEstado2);
        cuestionarioEstado2.setId(2L);
        assertThat(cuestionarioEstado1).isNotEqualTo(cuestionarioEstado2);
        cuestionarioEstado1.setId(null);
        assertThat(cuestionarioEstado1).isNotEqualTo(cuestionarioEstado2);
    }
}
