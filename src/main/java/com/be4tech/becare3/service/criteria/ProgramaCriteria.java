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
 * Criteria class for the {@link com.be4tech.becare3.domain.Programa} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.ProgramaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /programas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProgramaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter caloriasActividad;

    private IntegerFilter pasosActividad;

    private InstantFilter fechaRegistro;

    private StringFilter userId;

    public ProgramaCriteria() {}

    public ProgramaCriteria(ProgramaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.caloriasActividad = other.caloriasActividad == null ? null : other.caloriasActividad.copy();
        this.pasosActividad = other.pasosActividad == null ? null : other.pasosActividad.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public ProgramaCriteria copy() {
        return new ProgramaCriteria(this);
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

    public IntegerFilter getCaloriasActividad() {
        return caloriasActividad;
    }

    public IntegerFilter caloriasActividad() {
        if (caloriasActividad == null) {
            caloriasActividad = new IntegerFilter();
        }
        return caloriasActividad;
    }

    public void setCaloriasActividad(IntegerFilter caloriasActividad) {
        this.caloriasActividad = caloriasActividad;
    }

    public IntegerFilter getPasosActividad() {
        return pasosActividad;
    }

    public IntegerFilter pasosActividad() {
        if (pasosActividad == null) {
            pasosActividad = new IntegerFilter();
        }
        return pasosActividad;
    }

    public void setPasosActividad(IntegerFilter pasosActividad) {
        this.pasosActividad = pasosActividad;
    }

    public InstantFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public InstantFilter fechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = new InstantFilter();
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(InstantFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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
        final ProgramaCriteria that = (ProgramaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(caloriasActividad, that.caloriasActividad) &&
            Objects.equals(pasosActividad, that.pasosActividad) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caloriasActividad, pasosActividad, fechaRegistro, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (caloriasActividad != null ? "caloriasActividad=" + caloriasActividad + ", " : "") +
            (pasosActividad != null ? "pasosActividad=" + pasosActividad + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
