package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Encuesta;
import com.be4tech.becare3.repository.EncuestaRepository;
import com.be4tech.becare3.service.criteria.EncuestaCriteria;
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
 * Service for executing complex queries for {@link Encuesta} entities in the database.
 * The main input is a {@link EncuestaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Encuesta} or a {@link Page} of {@link Encuesta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EncuestaQueryService extends QueryService<Encuesta> {

    private final Logger log = LoggerFactory.getLogger(EncuestaQueryService.class);

    private final EncuestaRepository encuestaRepository;

    public EncuestaQueryService(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    /**
     * Return a {@link List} of {@link Encuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Encuesta> findByCriteria(EncuestaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Encuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Encuesta> findByCriteria(EncuestaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EncuestaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.count(specification);
    }

    /**
     * Function to convert {@link EncuestaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Encuesta> createSpecification(EncuestaCriteria criteria) {
        Specification<Encuesta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Encuesta_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Encuesta_.fecha));
            }
            if (criteria.getDebilidad() != null) {
                specification = specification.and(buildSpecification(criteria.getDebilidad(), Encuesta_.debilidad));
            }
            if (criteria.getCefalea() != null) {
                specification = specification.and(buildSpecification(criteria.getCefalea(), Encuesta_.cefalea));
            }
            if (criteria.getCalambres() != null) {
                specification = specification.and(buildSpecification(criteria.getCalambres(), Encuesta_.calambres));
            }
            if (criteria.getNauseas() != null) {
                specification = specification.and(buildSpecification(criteria.getNauseas(), Encuesta_.nauseas));
            }
            if (criteria.getVomito() != null) {
                specification = specification.and(buildSpecification(criteria.getVomito(), Encuesta_.vomito));
            }
            if (criteria.getMareo() != null) {
                specification = specification.and(buildSpecification(criteria.getMareo(), Encuesta_.mareo));
            }
            if (criteria.getNinguna() != null) {
                specification = specification.and(buildSpecification(criteria.getNinguna(), Encuesta_.ninguna));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Encuesta_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
