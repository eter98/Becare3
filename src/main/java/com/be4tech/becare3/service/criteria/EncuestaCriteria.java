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
 * Criteria class for the {@link com.be4tech.becare3.domain.Encuesta} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.EncuestaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /encuestas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EncuestaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fecha;

    private BooleanFilter debilidad;

    private BooleanFilter cefalea;

    private BooleanFilter calambres;

    private BooleanFilter nauseas;

    private BooleanFilter vomito;

    private BooleanFilter mareo;

    private BooleanFilter ninguna;

    private StringFilter userId;

    public EncuestaCriteria() {}

    public EncuestaCriteria(EncuestaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.debilidad = other.debilidad == null ? null : other.debilidad.copy();
        this.cefalea = other.cefalea == null ? null : other.cefalea.copy();
        this.calambres = other.calambres == null ? null : other.calambres.copy();
        this.nauseas = other.nauseas == null ? null : other.nauseas.copy();
        this.vomito = other.vomito == null ? null : other.vomito.copy();
        this.mareo = other.mareo == null ? null : other.mareo.copy();
        this.ninguna = other.ninguna == null ? null : other.ninguna.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public EncuestaCriteria copy() {
        return new EncuestaCriteria(this);
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

    public InstantFilter getFecha() {
        return fecha;
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            fecha = new InstantFilter();
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
    }

    public BooleanFilter getDebilidad() {
        return debilidad;
    }

    public BooleanFilter debilidad() {
        if (debilidad == null) {
            debilidad = new BooleanFilter();
        }
        return debilidad;
    }

    public void setDebilidad(BooleanFilter debilidad) {
        this.debilidad = debilidad;
    }

    public BooleanFilter getCefalea() {
        return cefalea;
    }

    public BooleanFilter cefalea() {
        if (cefalea == null) {
            cefalea = new BooleanFilter();
        }
        return cefalea;
    }

    public void setCefalea(BooleanFilter cefalea) {
        this.cefalea = cefalea;
    }

    public BooleanFilter getCalambres() {
        return calambres;
    }

    public BooleanFilter calambres() {
        if (calambres == null) {
            calambres = new BooleanFilter();
        }
        return calambres;
    }

    public void setCalambres(BooleanFilter calambres) {
        this.calambres = calambres;
    }

    public BooleanFilter getNauseas() {
        return nauseas;
    }

    public BooleanFilter nauseas() {
        if (nauseas == null) {
            nauseas = new BooleanFilter();
        }
        return nauseas;
    }

    public void setNauseas(BooleanFilter nauseas) {
        this.nauseas = nauseas;
    }

    public BooleanFilter getVomito() {
        return vomito;
    }

    public BooleanFilter vomito() {
        if (vomito == null) {
            vomito = new BooleanFilter();
        }
        return vomito;
    }

    public void setVomito(BooleanFilter vomito) {
        this.vomito = vomito;
    }

    public BooleanFilter getMareo() {
        return mareo;
    }

    public BooleanFilter mareo() {
        if (mareo == null) {
            mareo = new BooleanFilter();
        }
        return mareo;
    }

    public void setMareo(BooleanFilter mareo) {
        this.mareo = mareo;
    }

    public BooleanFilter getNinguna() {
        return ninguna;
    }

    public BooleanFilter ninguna() {
        if (ninguna == null) {
            ninguna = new BooleanFilter();
        }
        return ninguna;
    }

    public void setNinguna(BooleanFilter ninguna) {
        this.ninguna = ninguna;
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
        final EncuestaCriteria that = (EncuestaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(debilidad, that.debilidad) &&
            Objects.equals(cefalea, that.cefalea) &&
            Objects.equals(calambres, that.calambres) &&
            Objects.equals(nauseas, that.nauseas) &&
            Objects.equals(vomito, that.vomito) &&
            Objects.equals(mareo, that.mareo) &&
            Objects.equals(ninguna, that.ninguna) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, debilidad, cefalea, calambres, nauseas, vomito, mareo, ninguna, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EncuestaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (debilidad != null ? "debilidad=" + debilidad + ", " : "") +
            (cefalea != null ? "cefalea=" + cefalea + ", " : "") +
            (calambres != null ? "calambres=" + calambres + ", " : "") +
            (nauseas != null ? "nauseas=" + nauseas + ", " : "") +
            (vomito != null ? "vomito=" + vomito + ", " : "") +
            (mareo != null ? "mareo=" + mareo + ", " : "") +
            (ninguna != null ? "ninguna=" + ninguna + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
