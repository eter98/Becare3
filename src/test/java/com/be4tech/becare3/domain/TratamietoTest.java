package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TratamietoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tratamieto.class);
        Tratamieto tratamieto1 = new Tratamieto();
        tratamieto1.setId(1L);
        Tratamieto tratamieto2 = new Tratamieto();
        tratamieto2.setId(tratamieto1.getId());
        assertThat(tratamieto1).isEqualTo(tratamieto2);
        tratamieto2.setId(2L);
        assertThat(tratamieto1).isNotEqualTo(tratamieto2);
        tratamieto1.setId(null);
        assertThat(tratamieto1).isNotEqualTo(tratamieto2);
    }
}
