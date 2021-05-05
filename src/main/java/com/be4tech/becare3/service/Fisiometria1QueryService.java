package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Fisiometria1;
import com.be4tech.becare3.repository.Fisiometria1Repository;
import com.be4tech.becare3.service.criteria.Fisiometria1Criteria;
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
 * Service for executing complex queries for {@link Fisiometria1} entities in the database.
 * The main input is a {@link Fisiometria1Criteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Fisiometria1} or a {@link Page} of {@link Fisiometria1} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Fisiometria1QueryService extends QueryService<Fisiometria1> {

    private final Logger log = LoggerFactory.getLogger(Fisiometria1QueryService.class);

    private final Fisiometria1Repository fisiometria1Repository;

    public Fisiometria1QueryService(Fisiometria1Repository fisiometria1Repository) {
        this.fisiometria1Repository = fisiometria1Repository;
    }

    /**
     * Return a {@link List} of {@link Fisiometria1} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Fisiometria1> findByCriteria(Fisiometria1Criteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fisiometria1> specification = createSpecification(criteria);
        return fisiometria1Repository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Fisiometria1} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Fisiometria1> findByCriteria(Fisiometria1Criteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fisiometria1> specification = createSpecification(criteria);
        return fisiometria1Repository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Fisiometria1Criteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fisiometria1> specification = createSpecification(criteria);
        return fisiometria1Repository.count(specification);
    }

    /**
     * Function to convert {@link Fisiometria1Criteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fisiometria1> createSpecification(Fisiometria1Criteria criteria) {
        Specification<Fisiometria1> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fisiometria1_.id));
            }
            if (criteria.getRitmoCardiaco() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRitmoCardiaco(), Fisiometria1_.ritmoCardiaco));
            }
            if (criteria.getRitmoRespiratorio() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRitmoRespiratorio(), Fisiometria1_.ritmoRespiratorio));
            }
            if (criteria.getOximetria() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOximetria(), Fisiometria1_.oximetria));
            }
            if (criteria.getPresionArterialSistolica() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionArterialSistolica(), Fisiometria1_.presionArterialSistolica)
                    );
            }
            if (criteria.getPresionArterialDiastolica() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionArterialDiastolica(), Fisiometria1_.presionArterialDiastolica)
                    );
            }
            if (criteria.getTemperatura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTemperatura(), Fisiometria1_.temperatura));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Fisiometria1_.fechaRegistro));
            }
            if (criteria.getFechaToma() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaToma(), Fisiometria1_.fechaToma));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Fisiometria1_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
