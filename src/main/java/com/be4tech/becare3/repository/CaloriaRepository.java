package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Caloria;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Caloria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaloriaRepository extends JpaRepository<Caloria, Long>, JpaSpecificationExecutor<Caloria> {
    @Query("select caloria from Caloria caloria where caloria.user.login = ?#{principal.preferredUsername}")
    List<Caloria> findByUserIsCurrentUser();
}
