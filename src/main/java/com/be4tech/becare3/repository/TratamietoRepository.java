package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Tratamieto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tratamieto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TratamietoRepository extends JpaRepository<Tratamieto, Long>, JpaSpecificationExecutor<Tratamieto> {}
