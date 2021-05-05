package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Sueno;
import com.be4tech.becare3.repository.SuenoRepository;
import com.be4tech.becare3.service.criteria.SuenoCriteria;
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
 * Service for executing complex queries for {@link Sueno} entities in the database.
 * The main input is a {@link SuenoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sueno} or a {@link Page} of {@link Sueno} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuenoQueryService extends QueryService<Sueno> {

    private final Logger log = LoggerFactory.getLogger(SuenoQueryService.class);

    private final SuenoRepository suenoRepository;

    public SuenoQueryService(SuenoRepository suenoRepository) {
        this.suenoRepository = suenoRepository;
    }

    /**
     * Return a {@link List} of {@link Sueno} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sueno> findByCriteria(SuenoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sueno> specification = createSpecification(criteria);
        return suenoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sueno} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sueno> findByCriteria(SuenoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sueno> specification = createSpecification(criteria);
        return suenoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuenoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sueno> specification = createSpecification(criteria);
        return suenoRepository.count(specification);
    }

    /**
     * Function to convert {@link SuenoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sueno> createSpecification(SuenoCriteria criteria) {
        Specification<Sueno> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sueno_.id));
            }
            if (criteria.getSuperficial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuperficial(), Sueno_.superficial));
            }
            if (criteria.getProfundo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProfundo(), Sueno_.profundo));
            }
            if (criteria.getDespierto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDespierto(), Sueno_.despierto));
            }
            if (criteria.getTimeInstant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInstant(), Sueno_.timeInstant));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Sueno_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
