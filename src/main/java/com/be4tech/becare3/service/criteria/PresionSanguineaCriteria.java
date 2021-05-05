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
 * Criteria class for the {@link com.be4tech.becare3.domain.PresionSanguinea} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.PresionSanguineaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /presion-sanguineas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PresionSanguineaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter presionSanguineaSistolica;

    private IntegerFilter presionSanguineaDiastolica;

    private InstantFilter fechaRegistro;

    private StringFilter userId;

    public PresionSanguineaCriteria() {}

    public PresionSanguineaCriteria(PresionSanguineaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.presionSanguineaSistolica = other.presionSanguineaSistolica == null ? null : other.presionSanguineaSistolica.copy();
        this.presionSanguineaDiastolica = other.presionSanguineaDiastolica == null ? null : other.presionSanguineaDiastolica.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PresionSanguineaCriteria copy() {
        return new PresionSanguineaCriteria(this);
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

    public IntegerFilter getPresionSanguineaSistolica() {
        return presionSanguineaSistolica;
    }

    public IntegerFilter presionSanguineaSistolica() {
        if (presionSanguineaSistolica == null) {
            presionSanguineaSistolica = new IntegerFilter();
        }
        return presionSanguineaSistolica;
    }

    public void setPresionSanguineaSistolica(IntegerFilter presionSanguineaSistolica) {
        this.presionSanguineaSistolica = presionSanguineaSistolica;
    }

    public IntegerFilter getPresionSanguineaDiastolica() {
        return presionSanguineaDiastolica;
    }

    public IntegerFilter presionSanguineaDiastolica() {
        if (presionSanguineaDiastolica == null) {
            presionSanguineaDiastolica = new IntegerFilter();
        }
        return presionSanguineaDiastolica;
    }

    public void setPresionSanguineaDiastolica(IntegerFilter presionSanguineaDiastolica) {
        this.presionSanguineaDiastolica = presionSanguineaDiastolica;
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
        final PresionSanguineaCriteria that = (PresionSanguineaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(presionSanguineaSistolica, that.presionSanguineaSistolica) &&
            Objects.equals(presionSanguineaDiastolica, that.presionSanguineaDiastolica) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, presionSanguineaSistolica, presionSanguineaDiastolica, fechaRegistro, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresionSanguineaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (presionSanguineaSistolica != null ? "presionSanguineaSistolica=" + presionSanguineaSistolica + ", " : "") +
            (presionSanguineaDiastolica != null ? "presionSanguineaDiastolica=" + presionSanguineaDiastolica + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
