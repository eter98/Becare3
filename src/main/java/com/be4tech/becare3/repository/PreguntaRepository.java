package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Pregunta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pregunta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long>, JpaSpecificationExecutor<Pregunta> {}
