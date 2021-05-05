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
 * Criteria class for the {@link com.be4tech.becare3.domain.Alarma} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.AlarmaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alarmas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlarmaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter timeInstant;

    private StringFilter descripcion;

    private StringFilter procedimiento;

    private StringFilter titulo;

    private BooleanFilter verificar;

    private StringFilter observaciones;

    private StringFilter prioridad;

    private StringFilter userId;

    public AlarmaCriteria() {}

    public AlarmaCriteria(AlarmaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeInstant = other.timeInstant == null ? null : other.timeInstant.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.procedimiento = other.procedimiento == null ? null : other.procedimiento.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.verificar = other.verificar == null ? null : other.verificar.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.prioridad = other.prioridad == null ? null : other.prioridad.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public AlarmaCriteria copy() {
        return new AlarmaCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getProcedimiento() {
        return procedimiento;
    }

    public StringFilter procedimiento() {
        if (procedimiento == null) {
            procedimiento = new StringFilter();
        }
        return procedimiento;
    }

    public void setProcedimiento(StringFilter procedimiento) {
        this.procedimiento = procedimiento;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public BooleanFilter getVerificar() {
        return verificar;
    }

    public BooleanFilter verificar() {
        if (verificar == null) {
            verificar = new BooleanFilter();
        }
        return verificar;
    }

    public void setVerificar(BooleanFilter verificar) {
        this.verificar = verificar;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public StringFilter observaciones() {
        if (observaciones == null) {
            observaciones = new StringFilter();
        }
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
    }

    public StringFilter getPrioridad() {
        return prioridad;
    }

    public StringFilter prioridad() {
        if (prioridad == null) {
            prioridad = new StringFilter();
        }
        return prioridad;
    }

    public void setPrioridad(StringFilter prioridad) {
        this.prioridad = prioridad;
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
        final AlarmaCriteria that = (AlarmaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeInstant, that.timeInstant) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(procedimiento, that.procedimiento) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(verificar, that.verificar) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(prioridad, that.prioridad) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeInstant, descripcion, procedimiento, titulo, verificar, observaciones, prioridad, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlarmaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (timeInstant != null ? "timeInstant=" + timeInstant + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (procedimiento != null ? "procedimiento=" + procedimiento + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (verificar != null ? "verificar=" + verificar + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (prioridad != null ? "prioridad=" + prioridad + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
