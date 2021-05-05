package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingesta.class);
        Ingesta ingesta1 = new Ingesta();
        ingesta1.setId(1L);
        Ingesta ingesta2 = new Ingesta();
        ingesta2.setId(ingesta1.getId());
        assertThat(ingesta1).isEqualTo(ingesta2);
        ingesta2.setId(2L);
        assertThat(ingesta1).isNotEqualTo(ingesta2);
        ingesta1.setId(null);
        assertThat(ingesta1).isNotEqualTo(ingesta2);
    }
}
