package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Sueno;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sueno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuenoRepository extends JpaRepository<Sueno, Long>, JpaSpecificationExecutor<Sueno> {
    @Query("select sueno from Sueno sueno where sueno.user.login = ?#{principal.preferredUsername}")
    List<Sueno> findByUserIsCurrentUser();
}
