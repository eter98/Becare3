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
 * Criteria class for the {@link com.be4tech.becare3.domain.Sueno} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.SuenoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suenos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuenoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter superficial;

    private IntegerFilter profundo;

    private IntegerFilter despierto;

    private InstantFilter timeInstant;

    private StringFilter userId;

    public SuenoCriteria() {}

    public SuenoCriteria(SuenoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.superficial = other.superficial == null ? null : other.superficial.copy();
        this.profundo = other.profundo == null ? null : other.profundo.copy();
        this.despierto = other.despierto == null ? null : other.despierto.copy();
        this.timeInstant = other.timeInstant == null ? null : other.timeInstant.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public SuenoCriteria copy() {
        return new SuenoCriteria(this);
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

    public IntegerFilter getSuperficial() {
        return superficial;
    }

    public IntegerFilter superficial() {
        if (superficial == null) {
            superficial = new IntegerFilter();
        }
        return superficial;
    }

    public void setSuperficial(IntegerFilter superficial) {
        this.superficial = superficial;
    }

    public IntegerFilter getProfundo() {
        return profundo;
    }

    public IntegerFilter profundo() {
        if (profundo == null) {
            profundo = new IntegerFilter();
        }
        return profundo;
    }

    public void setProfundo(IntegerFilter profundo) {
        this.profundo = profundo;
    }

    public IntegerFilter getDespierto() {
        return despierto;
    }

    public IntegerFilter despierto() {
        if (despierto == null) {
            despierto = new IntegerFilter();
        }
        return despierto;
    }

    public void setDespierto(IntegerFilter despierto) {
        this.despierto = despierto;
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
        final SuenoCriteria that = (SuenoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(superficial, that.superficial) &&
            Objects.equals(profundo, that.profundo) &&
            Objects.equals(despierto, that.despierto) &&
            Objects.equals(timeInstant, that.timeInstant) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, superficial, profundo, despierto, timeInstant, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuenoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (superficial != null ? "superficial=" + superficial + ", " : "") +
            (profundo != null ? "profundo=" + profundo + ", " : "") +
            (despierto != null ? "despierto=" + despierto + ", " : "") +
            (timeInstant != null ? "timeInstant=" + timeInstant + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
