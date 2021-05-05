package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Caloria;
import com.be4tech.becare3.repository.CaloriaRepository;
import com.be4tech.becare3.service.criteria.CaloriaCriteria;
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
 * Service for executing complex queries for {@link Caloria} entities in the database.
 * The main input is a {@link CaloriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Caloria} or a {@link Page} of {@link Caloria} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CaloriaQueryService extends QueryService<Caloria> {

    private final Logger log = LoggerFactory.getLogger(CaloriaQueryService.class);

    private final CaloriaRepository caloriaRepository;

    public CaloriaQueryService(CaloriaRepository caloriaRepository) {
        this.caloriaRepository = caloriaRepository;
    }

    /**
     * Return a {@link List} of {@link Caloria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Caloria> findByCriteria(CaloriaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Caloria> specification = createSpecification(criteria);
        return caloriaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Caloria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Caloria> findByCriteria(CaloriaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Caloria> specification = createSpecification(criteria);
        return caloriaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CaloriaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Caloria> specification = createSpecification(criteria);
        return caloriaRepository.count(specification);
    }

    /**
     * Function to convert {@link CaloriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Caloria> createSpecification(CaloriaCriteria criteria) {
        Specification<Caloria> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Caloria_.id));
            }
            if (criteria.getCaloriasActivas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCaloriasActivas(), Caloria_.caloriasActivas));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Caloria_.descripcion));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Caloria_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Caloria_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
