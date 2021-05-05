package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Dispositivo;
import com.be4tech.becare3.repository.DispositivoRepository;
import com.be4tech.becare3.service.criteria.DispositivoCriteria;
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
 * Service for executing complex queries for {@link Dispositivo} entities in the database.
 * The main input is a {@link DispositivoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Dispositivo} or a {@link Page} of {@link Dispositivo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DispositivoQueryService extends QueryService<Dispositivo> {

    private final Logger log = LoggerFactory.getLogger(DispositivoQueryService.class);

    private final DispositivoRepository dispositivoRepository;

    public DispositivoQueryService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    /**
     * Return a {@link List} of {@link Dispositivo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Dispositivo> findByCriteria(DispositivoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dispositivo> specification = createSpecification(criteria);
        return dispositivoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Dispositivo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Dispositivo> findByCriteria(DispositivoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dispositivo> specification = createSpecification(criteria);
        return dispositivoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DispositivoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dispositivo> specification = createSpecification(criteria);
        return dispositivoRepository.count(specification);
    }

    /**
     * Function to convert {@link DispositivoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dispositivo> createSpecification(DispositivoCriteria criteria) {
        Specification<Dispositivo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dispositivo_.id));
            }
            if (criteria.getDispositivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDispositivo(), Dispositivo_.dispositivo));
            }
            if (criteria.getMac() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMac(), Dispositivo_.mac));
            }
            if (criteria.getConectado() != null) {
                specification = specification.and(buildSpecification(criteria.getConectado(), Dispositivo_.conectado));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Dispositivo_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
