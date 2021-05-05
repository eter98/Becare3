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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.be4tech.becare3.domain.Tratamieto} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.TratamietoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tratamietos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TratamietoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fechaInicio;

    private ZonedDateTimeFilter fechaFin;

    public TratamietoCriteria() {}

    public TratamietoCriteria(TratamietoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
    }

    @Override
    public TratamietoCriteria copy() {
        return new TratamietoCriteria(this);
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

    public InstantFilter getFechaInicio() {
        return fechaInicio;
    }

    public InstantFilter fechaInicio() {
        if (fechaInicio == null) {
            fechaInicio = new InstantFilter();
        }
        return fechaInicio;
    }

    public void setFechaInicio(InstantFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTimeFilter getFechaFin() {
        return fechaFin;
    }

    public ZonedDateTimeFilter fechaFin() {
        if (fechaFin == null) {
            fechaFin = new ZonedDateTimeFilter();
        }
        return fechaFin;
    }

    public void setFechaFin(ZonedDateTimeFilter fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TratamietoCriteria that = (TratamietoCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(fechaInicio, that.fechaInicio) && Objects.equals(fechaFin, that.fechaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaFin);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TratamietoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            "}";
    }
}
