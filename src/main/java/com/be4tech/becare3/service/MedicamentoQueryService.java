package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.repository.MedicamentoRepository;
import com.be4tech.becare3.service.criteria.MedicamentoCriteria;
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
 * Service for executing complex queries for {@link Medicamento} entities in the database.
 * The main input is a {@link MedicamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Medicamento} or a {@link Page} of {@link Medicamento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicamentoQueryService extends QueryService<Medicamento> {

    private final Logger log = LoggerFactory.getLogger(MedicamentoQueryService.class);

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoQueryService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * Return a {@link List} of {@link Medicamento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Medicamento> findByCriteria(MedicamentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medicamento> specification = createSpecification(criteria);
        return medicamentoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Medicamento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Medicamento> findByCriteria(MedicamentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicamento> specification = createSpecification(criteria);
        return medicamentoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicamentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medicamento> specification = createSpecification(criteria);
        return medicamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medicamento> createSpecification(MedicamentoCriteria criteria) {
        Specification<Medicamento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medicamento_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Medicamento_.nombre));
            }
            if (criteria.getFechaIngreso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaIngreso(), Medicamento_.fechaIngreso));
            }
            if (criteria.getPresentacion() != null) {
                specification = specification.and(buildSpecification(criteria.getPresentacion(), Medicamento_.presentacion));
            }
        }
        return specification;
    }
}
