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
 * Criteria class for the {@link com.be4tech.becare3.domain.FrecuenciaCardiaca} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.FrecuenciaCardiacaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /frecuencia-cardiacas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FrecuenciaCardiacaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter frecuenciaCardiaca;

    private InstantFilter fechaRegistro;

    private StringFilter userId;

    public FrecuenciaCardiacaCriteria() {}

    public FrecuenciaCardiacaCriteria(FrecuenciaCardiacaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.frecuenciaCardiaca = other.frecuenciaCardiaca == null ? null : other.frecuenciaCardiaca.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public FrecuenciaCardiacaCriteria copy() {
        return new FrecuenciaCardiacaCriteria(this);
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

    public IntegerFilter getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public IntegerFilter frecuenciaCardiaca() {
        if (frecuenciaCardiaca == null) {
            frecuenciaCardiaca = new IntegerFilter();
        }
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(IntegerFilter frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
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
        final FrecuenciaCardiacaCriteria that = (FrecuenciaCardiacaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(frecuenciaCardiaca, that.frecuenciaCardiaca) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, frecuenciaCardiaca, fechaRegistro, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrecuenciaCardiacaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (frecuenciaCardiaca != null ? "frecuenciaCardiaca=" + frecuenciaCardiaca + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
