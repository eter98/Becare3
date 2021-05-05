package com.be4tech.becare3.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.be4tech.becare3.domain.Agenda} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.AgendaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agenda?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgendaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter horaMedicamento;

    private LongFilter medicamentoId;

    public AgendaCriteria() {}

    public AgendaCriteria(AgendaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.horaMedicamento = other.horaMedicamento == null ? null : other.horaMedicamento.copy();
        this.medicamentoId = other.medicamentoId == null ? null : other.medicamentoId.copy();
    }

    @Override
    public AgendaCriteria copy() {
        return new AgendaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getHoraMedicamento() {
        return horaMedicamento;
    }

    public IntegerFilter horaMedicamento() {
        if (horaMedicamento == null) {
            horaMedicamento = new IntegerFilter();
        }
        return horaMedicamento;
    }

    public void setHoraMedicamento(IntegerFilter horaMedicamento) {
        this.horaMedicamento = horaMedicamento;
    }

    public LongFilter getMedicamentoId() {
        return medicamentoId;
    }

    public LongFilter medicamentoId() {
        if (medicamentoId == null) {
            medicamentoId = new LongFilter();
        }
        return medicamentoId;
    }

    public void setMedicamentoId(LongFilter medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AgendaCriteria that = (AgendaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(horaMedicamento, that.horaMedicamento) &&
            Objects.equals(medicamentoId, that.medicamentoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaMedicamento, medicamentoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (horaMedicamento != null ? "horaMedicamento=" + horaMedicamento + ", " : "") +
            (medicamentoId != null ? "medicamentoId=" + medicamentoId + ", " : "") +
            "}";
    }
}
