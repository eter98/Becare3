package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Programa;
import com.be4tech.becare3.repository.ProgramaRepository;
import com.be4tech.becare3.service.criteria.ProgramaCriteria;
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
 * Service for executing complex queries for {@link Programa} entities in the database.
 * The main input is a {@link ProgramaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Programa} or a {@link Page} of {@link Programa} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProgramaQueryService extends QueryService<Programa> {

    private final Logger log = LoggerFactory.getLogger(ProgramaQueryService.class);

    private final ProgramaRepository programaRepository;

    public ProgramaQueryService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    /**
     * Return a {@link List} of {@link Programa} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Programa> findByCriteria(ProgramaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Programa> specification = createSpecification(criteria);
        return programaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Programa} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Programa> findByCriteria(ProgramaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Programa> specification = createSpecification(criteria);
        return programaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProgramaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Programa> specification = createSpecification(criteria);
        return programaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProgramaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Programa> createSpecification(ProgramaCriteria criteria) {
        Specification<Programa> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Programa_.id));
            }
            if (criteria.getCaloriasActividad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCaloriasActividad(), Programa_.caloriasActividad));
            }
            if (criteria.getPasosActividad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPasosActividad(), Programa_.pasosActividad));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Programa_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Programa_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
