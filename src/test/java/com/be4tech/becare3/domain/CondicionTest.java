package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CondicionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Condicion.class);
        Condicion condicion1 = new Condicion();
        condicion1.setId(1L);
        Condicion condicion2 = new Condicion();
        condicion2.setId(condicion1.getId());
        assertThat(condicion1).isEqualTo(condicion2);
        condicion2.setId(2L);
        assertThat(condicion1).isNotEqualTo(condicion2);
        condicion1.setId(null);
        assertThat(condicion1).isNotEqualTo(condicion2);
    }
}
