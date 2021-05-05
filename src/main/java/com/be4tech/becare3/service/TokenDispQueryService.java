package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.TokenDisp;
import com.be4tech.becare3.repository.TokenDispRepository;
import com.be4tech.becare3.service.criteria.TokenDispCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TokenDisp} entities in the database.
 * The main input is a {@link TokenDispCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TokenDisp} or a {@link Page} of {@link TokenDisp} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TokenDispQueryService extends QueryService<TokenDisp> {

    private final Logger log = LoggerFactory.getLogger(TokenDispQueryService.class);

    private final TokenDispRepository tokenDispRepository;

    public TokenDispQueryService(TokenDispRepository tokenDispRepository) {
        this.tokenDispRepository = tokenDispRepository;
    }

    /**
     * Return a {@link List} of {@link TokenDisp} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TokenDisp> findByCriteria(TokenDispCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TokenDisp> specification = createSpecification(criteria);
        return tokenDispRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TokenDisp} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TokenDisp> findByCriteria(TokenDispCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TokenDisp> specification = createSpecification(criteria);
        return tokenDispRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TokenDispCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TokenDisp> specification = createSpecification(criteria);
        return tokenDispRepository.count(specification);
    }

    /**
     * Function to convert {@link TokenDispCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TokenDisp> createSpecification(TokenDispCriteria criteria) {
        Specification<TokenDisp> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TokenDisp_.id));
            }
            if (criteria.getTokenConexion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenConexion(), TokenDisp_.tokenConexion));
            }
            if (criteria.getActivo() != null) {
                specification = specification.and(buildSpecification(criteria.getActivo(), TokenDisp_.activo));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), TokenDisp_.fechaInicio));
            }
            if (criteria.getFechaFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFin(), TokenDisp_.fechaFin));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(TokenDisp_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
