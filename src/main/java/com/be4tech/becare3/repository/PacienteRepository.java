package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {
    @Query("select paciente from Paciente paciente where paciente.user.login = ?#{principal.preferredUsername}")
    List<Paciente> findByUserIsCurrentUser();
}
