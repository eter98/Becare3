package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.IPS;
import com.be4tech.becare3.repository.IPSRepository;
import com.be4tech.becare3.service.criteria.IPSCriteria;
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
 * Service for executing complex queries for {@link IPS} entities in the database.
 * The main input is a {@link IPSCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IPS} or a {@link Page} of {@link IPS} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IPSQueryService extends QueryService<IPS> {

    private final Logger log = LoggerFactory.getLogger(IPSQueryService.class);

    private final IPSRepository iPSRepository;

    public IPSQueryService(IPSRepository iPSRepository) {
        this.iPSRepository = iPSRepository;
    }

    /**
     * Return a {@link List} of {@link IPS} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IPS> findByCriteria(IPSCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IPS> specification = createSpecification(criteria);
        return iPSRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link IPS} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IPS> findByCriteria(IPSCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IPS> specification = createSpecification(criteria);
        return iPSRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IPSCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IPS> specification = createSpecification(criteria);
        return iPSRepository.count(specification);
    }

    /**
     * Function to convert {@link IPSCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IPS> createSpecification(IPSCriteria criteria) {
        Specification<IPS> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IPS_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), IPS_.nombre));
            }
            if (criteria.getNit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNit(), IPS_.nit));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), IPS_.direccion));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), IPS_.telefono));
            }
            if (criteria.getCorreoElectronico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorreoElectronico(), IPS_.correoElectronico));
            }
        }
        return specification;
    }
}
