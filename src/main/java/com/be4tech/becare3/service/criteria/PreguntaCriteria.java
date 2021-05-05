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
 * Criteria class for the {@link com.be4tech.becare3.domain.Pregunta} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.PreguntaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /preguntas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PreguntaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pregunta;

    private LongFilter condicionId;

    public PreguntaCriteria() {}

    public PreguntaCriteria(PreguntaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pregunta = other.pregunta == null ? null : other.pregunta.copy();
        this.condicionId = other.condicionId == null ? null : other.condicionId.copy();
    }

    @Override
    public PreguntaCriteria copy() {
        return new PreguntaCriteria(this);
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

    public StringFilter getPregunta() {
        return pregunta;
    }

    public StringFilter pregunta() {
        if (pregunta == null) {
            pregunta = new StringFilter();
        }
        return pregunta;
    }

    public void setPregunta(StringFilter pregunta) {
        this.pregunta = pregunta;
    }

    public LongFilter getCondicionId() {
        return condicionId;
    }

    public LongFilter condicionId() {
        if (condicionId == null) {
            condicionId = new LongFilter();
        }
        return condicionId;
    }

    public void setCondicionId(LongFilter condicionId) {
        this.condicionId = condicionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PreguntaCriteria that = (PreguntaCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(pregunta, that.pregunta) && Objects.equals(condicionId, that.condicionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pregunta, condicionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreguntaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pregunta != null ? "pregunta=" + pregunta + ", " : "") +
            (condicionId != null ? "condicionId=" + condicionId + ", " : "") +
            "}";
    }
}
