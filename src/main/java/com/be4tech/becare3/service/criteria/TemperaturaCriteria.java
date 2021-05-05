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
 * Criteria class for the {@link com.be4tech.becare3.domain.Temperatura} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.TemperaturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /temperaturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TemperaturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter temperatura;

    private InstantFilter fechaRegistro;

    private StringFilter userId;

    public TemperaturaCriteria() {}

    public TemperaturaCriteria(TemperaturaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.temperatura = other.temperatura == null ? null : other.temperatura.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public TemperaturaCriteria copy() {
        return new TemperaturaCriteria(this);
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

    public FloatFilter getTemperatura() {
        return temperatura;
    }

    public FloatFilter temperatura() {
        if (temperatura == null) {
            temperatura = new FloatFilter();
        }
        return temperatura;
    }

    public void setTemperatura(FloatFilter temperatura) {
        this.temperatura = temperatura;
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
        final TemperaturaCriteria that = (TemperaturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(temperatura, that.temperatura) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperatura, fechaRegistro, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemperaturaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (temperatura != null ? "temperatura=" + temperatura + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
