package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Peso;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Peso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PesoRepository extends JpaRepository<Peso, Long>, JpaSpecificationExecutor<Peso> {
    @Query("select peso from Peso peso where peso.user.login = ?#{principal.preferredUsername}")
    List<Peso> findByUserIsCurrentUser();
}
