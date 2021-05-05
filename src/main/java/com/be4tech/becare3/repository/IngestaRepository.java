package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Ingesta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ingesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngestaRepository extends JpaRepository<Ingesta, Long>, JpaSpecificationExecutor<Ingesta> {
    @Query("select ingesta from Ingesta ingesta where ingesta.user.login = ?#{principal.preferredUsername}")
    List<Ingesta> findByUserIsCurrentUser();
}
