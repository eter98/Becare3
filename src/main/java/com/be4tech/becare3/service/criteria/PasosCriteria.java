package com.be4tech.becare3.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.be4tech.becare3.domain.Pasos} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.PasosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pasos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PasosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nroPasos;

    private InstantFilter timeInstant;

    private StringFilter userId;

    public PasosCriteria() {}

    public PasosCriteria(PasosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nroPasos = other.nroPasos == null ? null : other.nroPasos.copy();
        this.timeInstant = other.timeInstant == null ? null : other.timeInstant.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PasosCriteria copy() {
        return new PasosCriteria(this);
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

    public IntegerFilter getNroPasos() {
        return nroPasos;
    }

    public IntegerFilter nroPasos() {
        if (nroPasos == null) {
            nroPasos = new IntegerFilter();
        }
        return nroPasos;
    }

    public void setNroPasos(IntegerFilter nroPasos) {
        this.nroPasos = nroPasos;
    }

    public InstantFilter getTimeInstant() {
        return timeInstant;
    }

    public InstantFilter timeInstant() {
        if (timeInstant == null) {
            timeInstant = new InstantFilter();
        }
        return timeInstant;
    }

    public void setTimeInstant(InstantFilter timeInstant) {
        this.timeInstant = timeInstant;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PasosCriteria that = (PasosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nroPasos, that.nroPasos) &&
            Objects.equals(timeInstant, that.timeInstant) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroPasos, timeInstant, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nroPasos != null ? "nroPasos=" + nroPasos + ", " : "") +
            (timeInstant != null ? "timeInstant=" + timeInstant + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
