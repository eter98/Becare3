package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmaceuticaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Farmaceutica.class);
        Farmaceutica farmaceutica1 = new Farmaceutica();
        farmaceutica1.setId(1L);
        Farmaceutica farmaceutica2 = new Farmaceutica();
        farmaceutica2.setId(farmaceutica1.getId());
        assertThat(farmaceutica1).isEqualTo(farmaceutica2);
        farmaceutica2.setId(2L);
        assertThat(farmaceutica1).isNotEqualTo(farmaceutica2);
        farmaceutica1.setId(null);
        assertThat(farmaceutica1).isNotEqualTo(farmaceutica2);
    }
}
