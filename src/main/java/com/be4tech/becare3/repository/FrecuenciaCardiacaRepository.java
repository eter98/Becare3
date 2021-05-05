package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.FrecuenciaCardiaca;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FrecuenciaCardiaca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrecuenciaCardiacaRepository
    extends JpaRepository<FrecuenciaCardiaca, Long>, JpaSpecificationExecutor<FrecuenciaCardiaca> {
    @Query(
        "select frecuenciaCardiaca from FrecuenciaCardiaca frecuenciaCardiaca where frecuenciaCardiaca.user.login = ?#{principal.preferredUsername}"
    )
    List<FrecuenciaCardiaca> findByUserIsCurrentUser();
}
