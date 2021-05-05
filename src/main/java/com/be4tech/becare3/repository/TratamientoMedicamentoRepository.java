package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.TratamientoMedicamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TratamientoMedicamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TratamientoMedicamentoRepository
    extends JpaRepository<TratamientoMedicamento, Long>, JpaSpecificationExecutor<TratamientoMedicamento> {}
