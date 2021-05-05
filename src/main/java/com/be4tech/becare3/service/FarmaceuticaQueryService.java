package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Farmaceutica;
import com.be4tech.becare3.repository.FarmaceuticaRepository;
import com.be4tech.becare3.service.criteria.FarmaceuticaCriteria;
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
 * Service for executing complex queries for {@link Farmaceutica} entities in the database.
 * The main input is a {@link FarmaceuticaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Farmaceutica} or a {@link Page} of {@link Farmaceutica} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmaceuticaQueryService extends QueryService<Farmaceutica> {

    private final Logger log = LoggerFactory.getLogger(FarmaceuticaQueryService.class);

    private final FarmaceuticaRepository farmaceuticaRepository;

    public FarmaceuticaQueryService(FarmaceuticaRepository farmaceuticaRepository) {
        this.farmaceuticaRepository = farmaceuticaRepository;
    }

    /**
     * Return a {@link List} of {@link Farmaceutica} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Farmaceutica> findByCriteria(FarmaceuticaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Farmaceutica> specification = createSpecification(criteria);
        return farmaceuticaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Farmaceutica} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Farmaceutica> findByCriteria(FarmaceuticaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Farmaceutica> specification = createSpecification(criteria);
        return farmaceuticaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmaceuticaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Farmaceutica> specification = createSpecification(criteria);
        return farmaceuticaRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmaceuticaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Farmaceutica> createSpecification(FarmaceuticaCriteria criteria) {
        Specification<Farmaceutica> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Farmaceutica_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Farmaceutica_.nombre));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Farmaceutica_.direccion));
            }
            if (criteria.getPropietario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPropietario(), Farmaceutica_.propietario));
            }
        }
        return specification;
    }
}
