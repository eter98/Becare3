package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.CuestionarioEstado;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CuestionarioEstado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuestionarioEstadoRepository
    extends JpaRepository<CuestionarioEstado, Long>, JpaSpecificationExecutor<CuestionarioEstado> {
    @Query(
        "select cuestionarioEstado from CuestionarioEstado cuestionarioEstado where cuestionarioEstado.user.login = ?#{principal.preferredUsername}"
    )
    List<CuestionarioEstado> findByUserIsCurrentUser();
}
