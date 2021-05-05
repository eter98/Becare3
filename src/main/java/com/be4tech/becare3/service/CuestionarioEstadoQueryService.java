package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.CuestionarioEstado;
import com.be4tech.becare3.repository.CuestionarioEstadoRepository;
import com.be4tech.becare3.service.criteria.CuestionarioEstadoCriteria;
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
 * Service for executing complex queries for {@link CuestionarioEstado} entities in the database.
 * The main input is a {@link CuestionarioEstadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CuestionarioEstado} or a {@link Page} of {@link CuestionarioEstado} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CuestionarioEstadoQueryService extends QueryService<CuestionarioEstado> {

    private final Logger log = LoggerFactory.getLogger(CuestionarioEstadoQueryService.class);

    private final CuestionarioEstadoRepository cuestionarioEstadoRepository;

    public CuestionarioEstadoQueryService(CuestionarioEstadoRepository cuestionarioEstadoRepository) {
        this.cuestionarioEstadoRepository = cuestionarioEstadoRepository;
    }

    /**
     * Return a {@link List} of {@link CuestionarioEstado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CuestionarioEstado> findByCriteria(CuestionarioEstadoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CuestionarioEstado> specification = createSpecification(criteria);
        return cuestionarioEstadoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CuestionarioEstado} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CuestionarioEstado> findByCriteria(CuestionarioEstadoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CuestionarioEstado> specification = createSpecification(criteria);
        return cuestionarioEstadoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CuestionarioEstadoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CuestionarioEstado> specification = createSpecification(criteria);
        return cuestionarioEstadoRepository.count(specification);
    }

    /**
     * Function to convert {@link CuestionarioEstadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CuestionarioEstado> createSpecification(CuestionarioEstadoCriteria criteria) {
        Specification<CuestionarioEstado> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CuestionarioEstado_.id));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), CuestionarioEstado_.valor));
            }
            if (criteria.getValoracion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValoracion(), CuestionarioEstado_.valoracion));
            }
            if (criteria.getPreguntaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPreguntaId(),
                            root -> root.join(CuestionarioEstado_.pregunta, JoinType.LEFT).get(Pregunta_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(CuestionarioEstado_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
