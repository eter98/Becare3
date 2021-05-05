package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresionSanguineaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresionSanguinea.class);
        PresionSanguinea presionSanguinea1 = new PresionSanguinea();
        presionSanguinea1.setId(1L);
        PresionSanguinea presionSanguinea2 = new PresionSanguinea();
        presionSanguinea2.setId(presionSanguinea1.getId());
        assertThat(presionSanguinea1).isEqualTo(presionSanguinea2);
        presionSanguinea2.setId(2L);
        assertThat(presionSanguinea1).isNotEqualTo(presionSanguinea2);
        presionSanguinea1.setId(null);
        assertThat(presionSanguinea1).isNotEqualTo(presionSanguinea2);
    }
}
