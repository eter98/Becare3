package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Pasos;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pasos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasosRepository extends JpaRepository<Pasos, Long>, JpaSpecificationExecutor<Pasos> {
    @Query("select pasos from Pasos pasos where pasos.user.login = ?#{principal.preferredUsername}")
    List<Pasos> findByUserIsCurrentUser();
}
