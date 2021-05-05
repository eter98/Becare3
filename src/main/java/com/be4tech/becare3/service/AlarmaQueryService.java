package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Alarma;
import com.be4tech.becare3.repository.AlarmaRepository;
import com.be4tech.becare3.service.criteria.AlarmaCriteria;
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
 * Service for executing complex queries for {@link Alarma} entities in the database.
 * The main input is a {@link AlarmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Alarma} or a {@link Page} of {@link Alarma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlarmaQueryService extends QueryService<Alarma> {

    private final Logger log = LoggerFactory.getLogger(AlarmaQueryService.class);

    private final AlarmaRepository alarmaRepository;

    public AlarmaQueryService(AlarmaRepository alarmaRepository) {
        this.alarmaRepository = alarmaRepository;
    }

    /**
     * Return a {@link List} of {@link Alarma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Alarma> findByCriteria(AlarmaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Alarma> specification = createSpecification(criteria);
        return alarmaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Alarma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Alarma> findByCriteria(AlarmaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alarma> specification = createSpecification(criteria);
        return alarmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlarmaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Alarma> specification = createSpecification(criteria);
        return alarmaRepository.count(specification);
    }

    /**
     * Function to convert {@link AlarmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alarma> createSpecification(AlarmaCriteria criteria) {
        Specification<Alarma> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Alarma_.id));
            }
            if (criteria.getTimeInstant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeInstant(), Alarma_.timeInstant));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Alarma_.descripcion));
            }
            if (criteria.getProcedimiento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProcedimiento(), Alarma_.procedimiento));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Alarma_.titulo));
            }
            if (criteria.getVerificar() != null) {
                specification = specification.and(buildSpecification(criteria.getVerificar(), Alarma_.verificar));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Alarma_.observaciones));
            }
            if (criteria.getPrioridad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrioridad(), Alarma_.prioridad));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Alarma_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
