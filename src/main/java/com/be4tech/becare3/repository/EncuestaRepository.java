package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Encuesta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Encuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long>, JpaSpecificationExecutor<Encuesta> {
    @Query("select encuesta from Encuesta encuesta where encuesta.user.login = ?#{principal.preferredUsername}")
    List<Encuesta> findByUserIsCurrentUser();
}
