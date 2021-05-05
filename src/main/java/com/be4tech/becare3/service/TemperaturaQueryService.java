package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Temperatura;
import com.be4tech.becare3.repository.TemperaturaRepository;
import com.be4tech.becare3.service.criteria.TemperaturaCriteria;
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
 * Service for executing complex queries for {@link Temperatura} entities in the database.
 * The main input is a {@link TemperaturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Temperatura} or a {@link Page} of {@link Temperatura} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemperaturaQueryService extends QueryService<Temperatura> {

    private final Logger log = LoggerFactory.getLogger(TemperaturaQueryService.class);

    private final TemperaturaRepository temperaturaRepository;

    public TemperaturaQueryService(TemperaturaRepository temperaturaRepository) {
        this.temperaturaRepository = temperaturaRepository;
    }

    /**
     * Return a {@link List} of {@link Temperatura} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Temperatura> findByCriteria(TemperaturaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Temperatura> specification = createSpecification(criteria);
        return temperaturaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Temperatura} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Temperatura> findByCriteria(TemperaturaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Temperatura> specification = createSpecification(criteria);
        return temperaturaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemperaturaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Temperatura> specification = createSpecification(criteria);
        return temperaturaRepository.count(specification);
    }

    /**
     * Function to convert {@link TemperaturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Temperatura> createSpecification(TemperaturaCriteria criteria) {
        Specification<Temperatura> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Temperatura_.id));
            }
            if (criteria.getTemperatura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTemperatura(), Temperatura_.temperatura));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Temperatura_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Temperatura_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
