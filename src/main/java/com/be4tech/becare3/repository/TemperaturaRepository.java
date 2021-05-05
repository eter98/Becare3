package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Temperatura;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Temperatura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemperaturaRepository extends JpaRepository<Temperatura, Long>, JpaSpecificationExecutor<Temperatura> {
    @Query("select temperatura from Temperatura temperatura where temperatura.user.login = ?#{principal.preferredUsername}")
    List<Temperatura> findByUserIsCurrentUser();
}
