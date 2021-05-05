package com.be4tech.becare3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.be4tech.becare3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TratamientoMedicamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TratamientoMedicamento.class);
        TratamientoMedicamento tratamientoMedicamento1 = new TratamientoMedicamento();
        tratamientoMedicamento1.setId(1L);
        TratamientoMedicamento tratamientoMedicamento2 = new TratamientoMedicamento();
        tratamientoMedicamento2.setId(tratamientoMedicamento1.getId());
        assertThat(tratamientoMedicamento1).isEqualTo(tratamientoMedicamento2);
        tratamientoMedicamento2.setId(2L);
        assertThat(tratamientoMedicamento1).isNotEqualTo(tratamientoMedicamento2);
        tratamientoMedicamento1.setId(null);
        assertThat(tratamientoMedicamento1).isNotEqualTo(tratamientoMedicamento2);
    }
}
