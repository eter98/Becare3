package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.IPS;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IPS entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IPSRepository extends JpaRepository<IPS, Long>, JpaSpecificationExecutor<IPS> {}
