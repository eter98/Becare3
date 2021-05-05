package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Alarma;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Alarma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlarmaRepository extends JpaRepository<Alarma, Long>, JpaSpecificationExecutor<Alarma> {
    @Query("select alarma from Alarma alarma where alarma.user.login = ?#{principal.preferredUsername}")
    List<Alarma> findByUserIsCurrentUser();
}
