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
 * Criteria class for the {@link com.be4tech.becare3.domain.Notificacion} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.NotificacionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notificacions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificacionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fechaInicio;

    private ZonedDateTimeFilter fechaActualizacion;

    private IntegerFilter estado;

    private IntegerFilter tipoNotificacion;

    private LongFilter tokenId;

    public NotificacionCriteria() {}

    public NotificacionCriteria(NotificacionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaActualizacion = other.fechaActualizacion == null ? null : other.fechaActualizacion.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.tipoNotificacion = other.tipoNotificacion == null ? null : other.tipoNotificacion.copy();
        this.tokenId = other.tokenId == null ? null : other.tokenId.copy();
    }

    @Override
    public NotificacionCriteria copy() {
        return new NotificacionCriteria(this);
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

    public ZonedDateTimeFilter getFechaActualizacion() {
        return fechaActualizacion;
    }

    public ZonedDateTimeFilter fechaActualizacion() {
        if (fechaActualizacion == null) {
            fechaActualizacion = new ZonedDateTimeFilter();
        }
        return fechaActualizacion;
    }

    public void setFechaActualizacion(ZonedDateTimeFilter fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public IntegerFilter getEstado() {
        return estado;
    }

    public IntegerFilter estado() {
        if (estado == null) {
            estado = new IntegerFilter();
        }
        return estado;
    }

    public void setEstado(IntegerFilter estado) {
        this.estado = estado;
    }

    public IntegerFilter getTipoNotificacion() {
        return tipoNotificacion;
    }

    public IntegerFilter tipoNotificacion() {
        if (tipoNotificacion == null) {
            tipoNotificacion = new IntegerFilter();
        }
        return tipoNotificacion;
    }

    public void setTipoNotificacion(IntegerFilter tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public LongFilter getTokenId() {
        return tokenId;
    }

    public LongFilter tokenId() {
        if (tokenId == null) {
            tokenId = new LongFilter();
        }
        return tokenId;
    }

    public void setTokenId(LongFilter tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificacionCriteria that = (NotificacionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaActualizacion, that.fechaActualizacion) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(tipoNotificacion, that.tipoNotificacion) &&
            Objects.equals(tokenId, that.tokenId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaActualizacion, estado, tipoNotificacion, tokenId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaActualizacion != null ? "fechaActualizacion=" + fechaActualizacion + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (tipoNotificacion != null ? "tipoNotificacion=" + tipoNotificacion + ", " : "") +
            (tokenId != null ? "tokenId=" + tokenId + ", " : "") +
            "}";
    }
}
