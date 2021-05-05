package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.repository.TratamietoRepository;
import com.be4tech.becare3.service.criteria.TratamietoCriteria;
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
 * Service for executing complex queries for {@link Tratamieto} entities in the database.
 * The main input is a {@link TratamietoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tratamieto} or a {@link Page} of {@link Tratamieto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TratamietoQueryService extends QueryService<Tratamieto> {

    private final Logger log = LoggerFactory.getLogger(TratamietoQueryService.class);

    private final TratamietoRepository tratamietoRepository;

    public TratamietoQueryService(TratamietoRepository tratamietoRepository) {
        this.tratamietoRepository = tratamietoRepository;
    }

    /**
     * Return a {@link List} of {@link Tratamieto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tratamieto> findByCriteria(TratamietoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tratamieto> specification = createSpecification(criteria);
        return tratamietoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tratamieto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tratamieto> findByCriteria(TratamietoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tratamieto> specification = createSpecification(criteria);
        return tratamietoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TratamietoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tratamieto> specification = createSpecification(criteria);
        return tratamietoRepository.count(specification);
    }

    /**
     * Function to convert {@link TratamietoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tratamieto> createSpecification(TratamietoCriteria criteria) {
        Specification<Tratamieto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tratamieto_.id));
            }
            if (criteria.getFechaInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaInicio(), Tratamieto_.fechaInicio));
            }
            if (criteria.getFechaFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFin(), Tratamieto_.fechaFin));
            }
        }
        return specification;
    }
}
