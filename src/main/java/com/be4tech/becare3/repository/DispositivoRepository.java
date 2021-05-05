package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Dispositivo;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dispositivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long>, JpaSpecificationExecutor<Dispositivo> {
    @Query("select dispositivo from Dispositivo dispositivo where dispositivo.user.login = ?#{principal.preferredUsername}")
    List<Dispositivo> findByUserIsCurrentUser();
}
