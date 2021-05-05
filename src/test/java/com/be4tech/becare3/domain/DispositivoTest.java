package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispositivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispositivo.class);
        Dispositivo dispositivo1 = new Dispositivo();
        dispositivo1.setId(1L);
        Dispositivo dispositivo2 = new Dispositivo();
        dispositivo2.setId(dispositivo1.getId());
        assertThat(dispositivo1).isEqualTo(dispositivo2);
        dispositivo2.setId(2L);
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);
        dispositivo1.setId(null);
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);
    }
}
