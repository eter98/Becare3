package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Programa;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Programa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long>, JpaSpecificationExecutor<Programa> {
    @Query("select programa from Programa programa where programa.user.login = ?#{principal.preferredUsername}")
    List<Programa> findByUserIsCurrentUser();
}
