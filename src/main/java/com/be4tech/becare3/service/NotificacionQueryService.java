package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Notificacion;
import com.be4tech.becare3.repository.NotificacionRepository;
import com.be4tech.becare3.service.criteria.NotificacionCriteria;
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
 * Service for executing complex queries for {@link Notificacion} entities in the database.
 * The main input is a {@link NotificacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Notificacion} or a {@link Page} of {@link Notificacion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificacionQueryService extends QueryService<Notificacion> {

    private final Logger log = LoggerFactory.getLogger(NotificacionQueryService.class);

    private final NotificacionRepository notificacionRepository;

    public NotificacionQueryService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Return a {@link List} of {@link Notificacion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Notificacion> findByCriteria(NotificacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notificacion> specification = createSpecification(criteria);
        return notificacionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Notificacion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Notificacion> findByCriteria(NotificacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notificacion> specification = createSpecification(criteria);
        return notificacionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notificacion> specification = createSpecification(criteria);
        return notificacionRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notificacion> createSpecification(NotificacionCriteria criteria) {
        Specification<Notificacion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notificacion_.id));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Notificacion_.fechaInicio));
            }
            if (criteria.getFechaActualizacion() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFechaActualizacion(), Notificacion_.fechaActualizacion));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstado(), Notificacion_.estado));
            }
            if (criteria.getTipoNotificacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTipoNotificacion(), Notificacion_.tipoNotificacion));
            }
            if (criteria.getTokenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTokenId(), root -> root.join(Notificacion_.token, JoinType.LEFT).get(TokenDisp_.id))
                    );
            }
        }
        return specification;
    }
}
