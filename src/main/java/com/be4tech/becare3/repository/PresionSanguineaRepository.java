package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.PresionSanguinea;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PresionSanguinea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresionSanguineaRepository extends JpaRepository<PresionSanguinea, Long>, JpaSpecificationExecutor<PresionSanguinea> {
    @Query(
        "select presionSanguinea from PresionSanguinea presionSanguinea where presionSanguinea.user.login = ?#{principal.preferredUsername}"
    )
    List<PresionSanguinea> findByUserIsCurrentUser();
}
