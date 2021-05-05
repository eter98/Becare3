package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Pasos;
import com.be4tech.becare3.repository.PasosRepository;
import com.be4tech.becare3.service.criteria.PasosCriteria;
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
 * Service for executing complex queries for {@link Pasos} entities in the database.
 * The main input is a {@link PasosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pasos} or a {@link Page} of {@link Pasos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PasosQueryService extends QueryService<Pasos> {

    private final Logger log = LoggerFactory.getLogger(PasosQueryService.class);

    private final PasosRepository pasosRepository;

    public PasosQueryService(PasosRepository pasosRepository) {
        this.pasosRepository = pasosRepository;
    }

    /**
     * Return a {@link List} of {@link Pasos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pasos> findByCriteria(PasosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pasos> specification = createSpecification(criteria);
        return pasosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pasos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pasos> findByCriteria(PasosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pasos> specification = createSpecification(criteria);
        return pasosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PasosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pasos> specification = createSpecification(criteria);
        return pasosRepository.count(specification);
    }

    /**
     * Function to convert {@link PasosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pasos> createSpecification(PasosCriteria criteria) {
        Specification<Pasos> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pasos_.id));
            }
            if (criteria.getNroPasos() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNroPasos(), Pasos_.nroPasos));
            }
            if (criteria.getTimeInstant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInstant(), Pasos_.timeInstant));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Pasos_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
