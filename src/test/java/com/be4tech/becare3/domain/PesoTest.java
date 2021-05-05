package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PesoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peso.class);
        Peso peso1 = new Peso();
        peso1.setId(1L);
        Peso peso2 = new Peso();
        peso2.setId(peso1.getId());
        assertThat(peso1).isEqualTo(peso2);
        peso2.setId(2L);
        assertThat(peso1).isNotEqualTo(peso2);
        peso1.setId(null);
        assertThat(peso1).isNotEqualTo(peso2);
    }
}
