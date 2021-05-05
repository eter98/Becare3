package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Condicion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Condicion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CondicionRepository extends JpaRepository<Condicion, Long>, JpaSpecificationExecutor<Condicion> {}
