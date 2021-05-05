package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.PresionSanguinea;
import com.be4tech.becare3.repository.PresionSanguineaRepository;
import com.be4tech.becare3.service.criteria.PresionSanguineaCriteria;
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
 * Service for executing complex queries for {@link PresionSanguinea} entities in the database.
 * The main input is a {@link PresionSanguineaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PresionSanguinea} or a {@link Page} of {@link PresionSanguinea} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PresionSanguineaQueryService extends QueryService<PresionSanguinea> {

    private final Logger log = LoggerFactory.getLogger(PresionSanguineaQueryService.class);

    private final PresionSanguineaRepository presionSanguineaRepository;

    public PresionSanguineaQueryService(PresionSanguineaRepository presionSanguineaRepository) {
        this.presionSanguineaRepository = presionSanguineaRepository;
    }

    /**
     * Return a {@link List} of {@link PresionSanguinea} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PresionSanguinea> findByCriteria(PresionSanguineaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PresionSanguinea> specification = createSpecification(criteria);
        return presionSanguineaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PresionSanguinea} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PresionSanguinea> findByCriteria(PresionSanguineaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PresionSanguinea> specification = createSpecification(criteria);
        return presionSanguineaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PresionSanguineaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PresionSanguinea> specification = createSpecification(criteria);
        return presionSanguineaRepository.count(specification);
    }

    /**
     * Function to convert {@link PresionSanguineaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PresionSanguinea> createSpecification(PresionSanguineaCriteria criteria) {
        Specification<PresionSanguinea> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PresionSanguinea_.id));
            }
            if (criteria.getPresionSanguineaSistolica() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionSanguineaSistolica(), PresionSanguinea_.presionSanguineaSistolica)
                    );
            }
            if (criteria.getPresionSanguineaDiastolica() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionSanguineaDiastolica(), PresionSanguinea_.presionSanguineaDiastolica)
                    );
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), PresionSanguinea_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(PresionSanguinea_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
