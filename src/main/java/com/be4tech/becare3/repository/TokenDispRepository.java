package com.be4tech.becare3.repository;

import com.be4tech.becare3.domain.TokenDisp;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TokenDisp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenDispRepository extends JpaRepository<TokenDisp, Long>, JpaSpecificationExecutor<TokenDisp> {
    @Query("select tokenDisp from TokenDisp tokenDisp where tokenDisp.user.login = ?#{principal.preferredUsername}")
    List<TokenDisp> findByUserIsCurrentUser();
}
