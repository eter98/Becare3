package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuenoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sueno.class);
        Sueno sueno1 = new Sueno();
        sueno1.setId(1L);
        Sueno sueno2 = new Sueno();
        sueno2.setId(sueno1.getId());
        assertThat(sueno1).isEqualTo(sueno2);
        sueno2.setId(2L);
        assertThat(sueno1).isNotEqualTo(sueno2);
        sueno1.setId(null);
        assertThat(sueno1).isNotEqualTo(sueno2);
    }
}
