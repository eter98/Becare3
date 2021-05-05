package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Pregunta;
import com.be4tech.becare3.repository.PreguntaRepository;
import com.be4tech.becare3.service.criteria.PreguntaCriteria;
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
 * Service for executing complex queries for {@link Pregunta} entities in the database.
 * The main input is a {@link PreguntaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pregunta} or a {@link Page} of {@link Pregunta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PreguntaQueryService extends QueryService<Pregunta> {

    private final Logger log = LoggerFactory.getLogger(PreguntaQueryService.class);

    private final PreguntaRepository preguntaRepository;

    public PreguntaQueryService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    /**
     * Return a {@link List} of {@link Pregunta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pregunta> findByCriteria(PreguntaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pregunta> specification = createSpecification(criteria);
        return preguntaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pregunta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pregunta> findByCriteria(PreguntaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pregunta> specification = createSpecification(criteria);
        return preguntaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreguntaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pregunta> specification = createSpecification(criteria);
        return preguntaRepository.count(specification);
    }

    /**
     * Function to convert {@link PreguntaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pregunta> createSpecification(PreguntaCriteria criteria) {
        Specification<Pregunta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pregunta_.id));
            }
            if (criteria.getPregunta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPregunta(), Pregunta_.pregunta));
            }
            if (criteria.getCondicionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCondicionId(),
                            root -> root.join(Pregunta_.condicion, JoinType.LEFT).get(Condicion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
