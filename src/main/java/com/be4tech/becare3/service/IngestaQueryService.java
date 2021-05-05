package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Ingesta;
import com.be4tech.becare3.repository.IngestaRepository;
import com.be4tech.becare3.service.criteria.IngestaCriteria;
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
 * Service for executing complex queries for {@link Ingesta} entities in the database.
 * The main input is a {@link IngestaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ingesta} or a {@link Page} of {@link Ingesta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngestaQueryService extends QueryService<Ingesta> {

    private final Logger log = LoggerFactory.getLogger(IngestaQueryService.class);

    private final IngestaRepository ingestaRepository;

    public IngestaQueryService(IngestaRepository ingestaRepository) {
        this.ingestaRepository = ingestaRepository;
    }

    /**
     * Return a {@link List} of {@link Ingesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ingesta> findByCriteria(IngestaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ingesta> specification = createSpecification(criteria);
        return ingestaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ingesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ingesta> findByCriteria(IngestaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ingesta> specification = createSpecification(criteria);
        return ingestaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IngestaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ingesta> specification = createSpecification(criteria);
        return ingestaRepository.count(specification);
    }

    /**
     * Function to convert {@link IngestaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ingesta> createSpecification(IngestaCriteria criteria) {
        Specification<Ingesta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ingesta_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Ingesta_.tipo));
            }
            if (criteria.getConsumoCalorias() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsumoCalorias(), Ingesta_.consumoCalorias));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Ingesta_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Ingesta_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
