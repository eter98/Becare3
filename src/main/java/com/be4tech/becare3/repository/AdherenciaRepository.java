package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Adherencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Adherencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdherenciaRepository extends JpaRepository<Adherencia, Long>, JpaSpecificationExecutor<Adherencia> {}
