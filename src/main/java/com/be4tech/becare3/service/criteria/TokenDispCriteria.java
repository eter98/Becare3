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
 * Criteria class for the {@link com.be4tech.becare3.domain.TokenDisp} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.TokenDispResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /token-disps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TokenDispCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tokenConexion;

    private BooleanFilter activo;

    private InstantFilter fechaInicio;

    private ZonedDateTimeFilter fechaFin;

    private StringFilter userId;

    public TokenDispCriteria() {}

    public TokenDispCriteria(TokenDispCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tokenConexion = other.tokenConexion == null ? null : other.tokenConexion.copy();
        this.activo = other.activo == null ? null : other.activo.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public TokenDispCriteria copy() {
        return new TokenDispCriteria(this);
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

    public StringFilter getTokenConexion() {
        return tokenConexion;
    }

    public StringFilter tokenConexion() {
        if (tokenConexion == null) {
            tokenConexion = new StringFilter();
        }
        return tokenConexion;
    }

    public void setTokenConexion(StringFilter tokenConexion) {
        this.tokenConexion = tokenConexion;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public BooleanFilter activo() {
        if (activo == null) {
            activo = new BooleanFilter();
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
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
        final TokenDispCriteria that = (TokenDispCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tokenConexion, that.tokenConexion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenConexion, activo, fechaInicio, fechaFin, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TokenDispCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tokenConexion != null ? "tokenConexion=" + tokenConexion + ", " : "") +
            (activo != null ? "activo=" + activo + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
