package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Fisiometria1;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fisiometria1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Fisiometria1Repository extends JpaRepository<Fisiometria1, Long>, JpaSpecificationExecutor<Fisiometria1> {
    @Query("select fisiometria1 from Fisiometria1 fisiometria1 where fisiometria1.user.login = ?#{principal.preferredUsername}")
    List<Fisiometria1> findByUserIsCurrentUser();
}
