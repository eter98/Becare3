package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.*; // for static metamodels
import com.be4tech.becare3.domain.Paciente;
import com.be4tech.becare3.repository.PacienteRepository;
import com.be4tech.becare3.service.criteria.PacienteCriteria;
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
 * Service for executing complex queries for {@link Paciente} entities in the database.
 * The main input is a {@link PacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Paciente} or a {@link Page} of {@link Paciente} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PacienteQueryService extends QueryService<Paciente> {

    private final Logger log = LoggerFactory.getLogger(PacienteQueryService.class);

    private final PacienteRepository pacienteRepository;

    public PacienteQueryService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Return a {@link List} of {@link Paciente} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Paciente> findByCriteria(PacienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Paciente} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Paciente> findByCriteria(PacienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PacienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link PacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paciente> createSpecification(PacienteCriteria criteria) {
        Specification<Paciente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Paciente_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Paciente_.nombre));
            }
            if (criteria.getTipoIdentificacion() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoIdentificacion(), Paciente_.tipoIdentificacion));
            }
            if (criteria.getIdentificacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdentificacion(), Paciente_.identificacion));
            }
            if (criteria.getEdad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEdad(), Paciente_.edad));
            }
            if (criteria.getSexo() != null) {
                specification = specification.and(buildSpecification(criteria.getSexo(), Paciente_.sexo));
            }
            if (criteria.getPesoKG() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPesoKG(), Paciente_.pesoKG));
            }
            if (criteria.getEstaturaCM() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstaturaCM(), Paciente_.estaturaCM));
            }
            if (criteria.getOximetriaReferencia() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOximetriaReferencia(), Paciente_.oximetriaReferencia));
            }
            if (criteria.getTemperaturaReferencia() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTemperaturaReferencia(), Paciente_.temperaturaReferencia));
            }
            if (criteria.getRitmoCardiacoReferencia() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRitmoCardiacoReferencia(), Paciente_.ritmoCardiacoReferencia));
            }
            if (criteria.getPresionSistolicaReferencia() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionSistolicaReferencia(), Paciente_.presionSistolicaReferencia)
                    );
            }
            if (criteria.getPresionDistolicaReferencia() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPresionDistolicaReferencia(), Paciente_.presionDistolicaReferencia)
                    );
            }
            if (criteria.getPasosReferencia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPasosReferencia(), Paciente_.pasosReferencia));
            }
            if (criteria.getCaloriasReferencia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCaloriasReferencia(), Paciente_.caloriasReferencia));
            }
            if (criteria.getMetaReferencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMetaReferencia(), Paciente_.metaReferencia));
            }
            if (criteria.getCondicionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCondicionId(),
                            root -> root.join(Paciente_.condicion, JoinType.LEFT).get(Condicion_.id)
                        )
                    );
            }
            if (criteria.getIpsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIpsId(), root -> root.join(Paciente_.ips, JoinType.LEFT).get(IPS_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Paciente_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTratamientoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTratamientoId(),
                            root -> root.join(Paciente_.tratamiento, JoinType.LEFT).get(Tratamieto_.id)
                        )
                    );
            }
            if (criteria.getFarmaceuticaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFarmaceuticaId(),
                            root -> root.join(Paciente_.farmaceutica, JoinType.LEFT).get(Farmaceutica_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
