package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.FrecuenciaCardiaca;
import com.be4tech.becare3.repository.FrecuenciaCardiacaRepository;
import com.be4tech.becare3.service.criteria.FrecuenciaCardiacaCriteria;
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
 * Service for executing complex queries for {@link FrecuenciaCardiaca} entities in the database.
 * The main input is a {@link FrecuenciaCardiacaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FrecuenciaCardiaca} or a {@link Page} of {@link FrecuenciaCardiaca} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FrecuenciaCardiacaQueryService extends QueryService<FrecuenciaCardiaca> {

    private final Logger log = LoggerFactory.getLogger(FrecuenciaCardiacaQueryService.class);

    private final FrecuenciaCardiacaRepository frecuenciaCardiacaRepository;

    public FrecuenciaCardiacaQueryService(FrecuenciaCardiacaRepository frecuenciaCardiacaRepository) {
        this.frecuenciaCardiacaRepository = frecuenciaCardiacaRepository;
    }

    /**
     * Return a {@link List} of {@link FrecuenciaCardiaca} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FrecuenciaCardiaca> findByCriteria(FrecuenciaCardiacaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FrecuenciaCardiaca> specification = createSpecification(criteria);
        return frecuenciaCardiacaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FrecuenciaCardiaca} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FrecuenciaCardiaca> findByCriteria(FrecuenciaCardiacaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FrecuenciaCardiaca> specification = createSpecification(criteria);
        return frecuenciaCardiacaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FrecuenciaCardiacaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FrecuenciaCardiaca> specification = createSpecification(criteria);
        return frecuenciaCardiacaRepository.count(specification);
    }

    /**
     * Function to convert {@link FrecuenciaCardiacaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FrecuenciaCardiaca> createSpecification(FrecuenciaCardiacaCriteria criteria) {
        Specification<FrecuenciaCardiaca> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FrecuenciaCardiaca_.id));
            }
            if (criteria.getFrecuenciaCardiaca() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFrecuenciaCardiaca(), FrecuenciaCardiaca_.frecuenciaCardiaca));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), FrecuenciaCardiaca_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(FrecuenciaCardiaca_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
