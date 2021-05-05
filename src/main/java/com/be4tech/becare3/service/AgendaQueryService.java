package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Agenda;
import com.be4tech.becare3.repository.AgendaRepository;
import com.be4tech.becare3.service.criteria.AgendaCriteria;
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
 * Service for executing complex queries for {@link Agenda} entities in the database.
 * The main input is a {@link AgendaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Agenda} or a {@link Page} of {@link Agenda} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgendaQueryService extends QueryService<Agenda> {

    private final Logger log = LoggerFactory.getLogger(AgendaQueryService.class);

    private final AgendaRepository agendaRepository;

    public AgendaQueryService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    /**
     * Return a {@link List} of {@link Agenda} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Agenda> findByCriteria(AgendaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agenda> specification = createSpecification(criteria);
        return agendaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Agenda} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Agenda> findByCriteria(AgendaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agenda> specification = createSpecification(criteria);
        return agendaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgendaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agenda> specification = createSpecification(criteria);
        return agendaRepository.count(specification);
    }

    /**
     * Function to convert {@link AgendaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agenda> createSpecification(AgendaCriteria criteria) {
        Specification<Agenda> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agenda_.id));
            }
            if (criteria.getHoraMedicamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHoraMedicamento(), Agenda_.horaMedicamento));
            }
            if (criteria.getMedicamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMedicamentoId(),
                            root -> root.join(Agenda_.medicamento, JoinType.LEFT).get(Medicamento_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
