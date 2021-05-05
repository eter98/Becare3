package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Peso;
import com.be4tech.becare3.repository.PesoRepository;
import com.be4tech.becare3.service.criteria.PesoCriteria;
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
 * Service for executing complex queries for {@link Peso} entities in the database.
 * The main input is a {@link PesoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Peso} or a {@link Page} of {@link Peso} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PesoQueryService extends QueryService<Peso> {

    private final Logger log = LoggerFactory.getLogger(PesoQueryService.class);

    private final PesoRepository pesoRepository;

    public PesoQueryService(PesoRepository pesoRepository) {
        this.pesoRepository = pesoRepository;
    }

    /**
     * Return a {@link List} of {@link Peso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Peso> findByCriteria(PesoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Peso> specification = createSpecification(criteria);
        return pesoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Peso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Peso> findByCriteria(PesoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Peso> specification = createSpecification(criteria);
        return pesoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PesoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Peso> specification = createSpecification(criteria);
        return pesoRepository.count(specification);
    }

    /**
     * Function to convert {@link PesoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Peso> createSpecification(PesoCriteria criteria) {
        Specification<Peso> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Peso_.id));
            }
            if (criteria.getPesoKG() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPesoKG(), Peso_.pesoKG));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Peso_.descripcion));
            }
            if (criteria.getFechaRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaRegistro(), Peso_.fechaRegistro));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Peso_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
