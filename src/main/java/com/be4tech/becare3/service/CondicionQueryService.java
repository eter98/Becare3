package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.repository.CondicionRepository;
import com.be4tech.becare3.service.criteria.CondicionCriteria;
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
 * Service for executing complex queries for {@link Condicion} entities in the database.
 * The main input is a {@link CondicionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Condicion} or a {@link Page} of {@link Condicion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CondicionQueryService extends QueryService<Condicion> {

    private final Logger log = LoggerFactory.getLogger(CondicionQueryService.class);

    private final CondicionRepository condicionRepository;

    public CondicionQueryService(CondicionRepository condicionRepository) {
        this.condicionRepository = condicionRepository;
    }

    /**
     * Return a {@link List} of {@link Condicion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Condicion> findByCriteria(CondicionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Condicion> specification = createSpecification(criteria);
        return condicionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Condicion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Condicion> findByCriteria(CondicionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Condicion> specification = createSpecification(criteria);
        return condicionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CondicionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Condicion> specification = createSpecification(criteria);
        return condicionRepository.count(specification);
    }

    /**
     * Function to convert {@link CondicionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Condicion> createSpecification(CondicionCriteria criteria) {
        Specification<Condicion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Condicion_.id));
            }
            if (criteria.getCondicion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCondicion(), Condicion_.condicion));
            }
        }
        return specification;
    }
}
