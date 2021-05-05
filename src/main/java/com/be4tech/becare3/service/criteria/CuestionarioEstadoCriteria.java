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
 * Criteria class for the {@link com.be4tech.becare3.domain.CuestionarioEstado} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.CuestionarioEstadoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cuestionario-estados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CuestionarioEstadoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter valor;

    private StringFilter valoracion;

    private LongFilter preguntaId;

    private StringFilter userId;

    public CuestionarioEstadoCriteria() {}

    public CuestionarioEstadoCriteria(CuestionarioEstadoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.valoracion = other.valoracion == null ? null : other.valoracion.copy();
        this.preguntaId = other.preguntaId == null ? null : other.preguntaId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public CuestionarioEstadoCriteria copy() {
        return new CuestionarioEstadoCriteria(this);
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

    public IntegerFilter getValor() {
        return valor;
    }

    public IntegerFilter valor() {
        if (valor == null) {
            valor = new IntegerFilter();
        }
        return valor;
    }

    public void setValor(IntegerFilter valor) {
        this.valor = valor;
    }

    public StringFilter getValoracion() {
        return valoracion;
    }

    public StringFilter valoracion() {
        if (valoracion == null) {
            valoracion = new StringFilter();
        }
        return valoracion;
    }

    public void setValoracion(StringFilter valoracion) {
        this.valoracion = valoracion;
    }

    public LongFilter getPreguntaId() {
        return preguntaId;
    }

    public LongFilter preguntaId() {
        if (preguntaId == null) {
            preguntaId = new LongFilter();
        }
        return preguntaId;
    }

    public void setPreguntaId(LongFilter preguntaId) {
        this.preguntaId = preguntaId;
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
        final CuestionarioEstadoCriteria that = (CuestionarioEstadoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(valoracion, that.valoracion) &&
            Objects.equals(preguntaId, that.preguntaId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, valoracion, preguntaId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuestionarioEstadoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (valoracion != null ? "valoracion=" + valoracion + ", " : "") +
            (preguntaId != null ? "preguntaId=" + preguntaId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
