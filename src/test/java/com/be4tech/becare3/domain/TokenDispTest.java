package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TokenDispTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenDisp.class);
        TokenDisp tokenDisp1 = new TokenDisp();
        tokenDisp1.setId(1L);
        TokenDisp tokenDisp2 = new TokenDisp();
        tokenDisp2.setId(tokenDisp1.getId());
        assertThat(tokenDisp1).isEqualTo(tokenDisp2);
        tokenDisp2.setId(2L);
        assertThat(tokenDisp1).isNotEqualTo(tokenDisp2);
        tokenDisp1.setId(null);
        assertThat(tokenDisp1).isNotEqualTo(tokenDisp2);
    }
}
