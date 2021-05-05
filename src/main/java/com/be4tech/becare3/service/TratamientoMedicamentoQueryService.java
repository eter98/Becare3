package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.TratamientoMedicamento;
import com.be4tech.becare3.repository.TratamientoMedicamentoRepository;
import com.be4tech.becare3.service.criteria.TratamientoMedicamentoCriteria;
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
 * Service for executing complex queries for {@link TratamientoMedicamento} entities in the database.
 * The main input is a {@link TratamientoMedicamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TratamientoMedicamento} or a {@link Page} of {@link TratamientoMedicamento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TratamientoMedicamentoQueryService extends QueryService<TratamientoMedicamento> {

    private final Logger log = LoggerFactory.getLogger(TratamientoMedicamentoQueryService.class);

    private final TratamientoMedicamentoRepository tratamientoMedicamentoRepository;

    public TratamientoMedicamentoQueryService(TratamientoMedicamentoRepository tratamientoMedicamentoRepository) {
        this.tratamientoMedicamentoRepository = tratamientoMedicamentoRepository;
    }

    /**
     * Return a {@link List} of {@link TratamientoMedicamento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TratamientoMedicamento> findByCriteria(TratamientoMedicamentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TratamientoMedicamento> specification = createSpecification(criteria);
        return tratamientoMedicamentoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TratamientoMedicamento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TratamientoMedicamento> findByCriteria(TratamientoMedicamentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TratamientoMedicamento> specification = createSpecification(criteria);
        return tratamientoMedicamentoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TratamientoMedicamentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TratamientoMedicamento> specification = createSpecification(criteria);
        return tratamientoMedicamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link TratamientoMedicamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TratamientoMedicamento> createSpecification(TratamientoMedicamentoCriteria criteria) {
        Specification<TratamientoMedicamento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TratamientoMedicamento_.id));
            }
            if (criteria.getDosis() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDosis(), TratamientoMedicamento_.dosis));
            }
            if (criteria.getIntensidad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIntensidad(), TratamientoMedicamento_.intensidad));
            }
            if (criteria.getTratamietoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTratamietoId(),
                            root -> root.join(TratamientoMedicamento_.tratamieto, JoinType.LEFT).get(Tratamieto_.id)
                        )
                    );
            }
            if (criteria.getMedicamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMedicamentoId(),
                            root -> root.join(TratamientoMedicamento_.medicamento, JoinType.LEFT).get(Medicamento_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
