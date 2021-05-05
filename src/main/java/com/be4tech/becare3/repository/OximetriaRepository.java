package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Oximetria;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Oximetria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OximetriaRepository extends JpaRepository<Oximetria, Long>, JpaSpecificationExecutor<Oximetria> {
    @Query("select oximetria from Oximetria oximetria where oximetria.user.login = ?#{principal.preferredUsername}")
    List<Oximetria> findByUserIsCurrentUser();
}
