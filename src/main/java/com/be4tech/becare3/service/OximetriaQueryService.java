package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Oximetria;
import com.be4tech.becare3.repository.OximetriaRepository;
import com.be4tech.becare3.service.criteria.OximetriaCriteria;
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
 * Service for executing complex queries for {@link Oximetria} entities in the database.
 * The main input is a {@link OximetriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Oximetria} or a {@link Page} of {@link Oximetria} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OximetriaQueryService extends QueryService<Oximetria> {

    private final Logger log = LoggerFactory.getLogger(OximetriaQueryService.class);

    private final OximetriaRepository oximetriaRepository;

    public OximetriaQueryService(OximetriaRepository oximetriaRepository) {
        this.oximetriaRepository = oximetriaRepository;
    }

    /**
     * Return a {@link List} of {@link Oximetria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Oximetria> findByCriteria(OximetriaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Oximetria> specification = createSpecification(criteria);
        return oximetriaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Oximetria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Oximetria> findByCriteria(OximetriaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Oximetria> specification = createSpecification(criteria);
        return oximetriaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OximetriaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Oximetria> specification = createSpecification(criteria);
        return oximetriaRepository.count(specification);
    }

    /**
     * Function to convert {@link OximetriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Oximetria> createSpecification(OximetriaCriteria criteria) {
        Specification<Oximetria> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Oximetria_.id));
            }
            if (criteria.getOximetria() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOximetria(), Oximetria_.oximetria));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Oximetria_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Oximetria_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
