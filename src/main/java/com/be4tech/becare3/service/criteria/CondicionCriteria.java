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
 * Criteria class for the {@link com.be4tech.becare3.domain.Condicion} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.CondicionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /condicions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CondicionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter condicion;

    public CondicionCriteria() {}

    public CondicionCriteria(CondicionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.condicion = other.condicion == null ? null : other.condicion.copy();
    }

    @Override
    public CondicionCriteria copy() {
        return new CondicionCriteria(this);
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

    public StringFilter getCondicion() {
        return condicion;
    }

    public StringFilter condicion() {
        if (condicion == null) {
            condicion = new StringFilter();
        }
        return condicion;
    }

    public void setCondicion(StringFilter condicion) {
        this.condicion = condicion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CondicionCriteria that = (CondicionCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(condicion, that.condicion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, condicion);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CondicionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (condicion != null ? "condicion=" + condicion + ", " : "") +
            "}";
    }
}
