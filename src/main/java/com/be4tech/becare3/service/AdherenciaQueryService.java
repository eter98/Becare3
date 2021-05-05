package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Adherencia;
import com.be4tech.becare3.repository.AdherenciaRepository;
import com.be4tech.becare3.service.criteria.AdherenciaCriteria;
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
 * Service for executing complex queries for {@link Adherencia} entities in the database.
 * The main input is a {@link AdherenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Adherencia} or a {@link Page} of {@link Adherencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdherenciaQueryService extends QueryService<Adherencia> {

    private final Logger log = LoggerFactory.getLogger(AdherenciaQueryService.class);

    private final AdherenciaRepository adherenciaRepository;

    public AdherenciaQueryService(AdherenciaRepository adherenciaRepository) {
        this.adherenciaRepository = adherenciaRepository;
    }

    /**
     * Return a {@link List} of {@link Adherencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Adherencia> findByCriteria(AdherenciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Adherencia> specification = createSpecification(criteria);
        return adherenciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Adherencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Adherencia> findByCriteria(AdherenciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Adherencia> specification = createSpecification(criteria);
        return adherenciaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdherenciaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Adherencia> specification = createSpecification(criteria);
        return adherenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link AdherenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Adherencia> createSpecification(AdherenciaCriteria criteria) {
        Specification<Adherencia> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Adherencia_.id));
            }
            if (criteria.getHoraToma() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHoraToma(), Adherencia_.horaToma));
            }
            if (criteria.getRespuesta() != null) {
                specification = specification.and(buildSpecification(criteria.getRespuesta(), Adherencia_.respuesta));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Adherencia_.valor));
            }
            if (criteria.getComentario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComentario(), Adherencia_.comentario));
            }
            if (criteria.getMedicamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMedicamentoId(),
                            root -> root.join(Adherencia_.medicamento, JoinType.LEFT).get(Medicamento_.id)
                        )
                    );
            }
            if (criteria.getPacienteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPacienteId(),
                            root -> root.join(Adherencia_.paciente, JoinType.LEFT).get(Paciente_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
