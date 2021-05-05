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
 * Criteria class for the {@link com.be4tech.becare3.domain.Fisiometria1} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.Fisiometria1Resource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fisiometria-1-s?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class Fisiometria1Criteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter ritmoCardiaco;

    private IntegerFilter ritmoRespiratorio;

    private IntegerFilter oximetria;

    private IntegerFilter presionArterialSistolica;

    private IntegerFilter presionArterialDiastolica;

    private FloatFilter temperatura;

    private InstantFilter fechaRegistro;

    private InstantFilter fechaToma;

    private StringFilter userId;

    public Fisiometria1Criteria() {}

    public Fisiometria1Criteria(Fisiometria1Criteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ritmoCardiaco = other.ritmoCardiaco == null ? null : other.ritmoCardiaco.copy();
        this.ritmoRespiratorio = other.ritmoRespiratorio == null ? null : other.ritmoRespiratorio.copy();
        this.oximetria = other.oximetria == null ? null : other.oximetria.copy();
        this.presionArterialSistolica = other.presionArterialSistolica == null ? null : other.presionArterialSistolica.copy();
        this.presionArterialDiastolica = other.presionArterialDiastolica == null ? null : other.presionArterialDiastolica.copy();
        this.temperatura = other.temperatura == null ? null : other.temperatura.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.fechaToma = other.fechaToma == null ? null : other.fechaToma.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public Fisiometria1Criteria copy() {
        return new Fisiometria1Criteria(this);
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

    public IntegerFilter getRitmoCardiaco() {
        return ritmoCardiaco;
    }

    public IntegerFilter ritmoCardiaco() {
        if (ritmoCardiaco == null) {
            ritmoCardiaco = new IntegerFilter();
        }
        return ritmoCardiaco;
    }

    public void setRitmoCardiaco(IntegerFilter ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
    }

    public IntegerFilter getRitmoRespiratorio() {
        return ritmoRespiratorio;
    }

    public IntegerFilter ritmoRespiratorio() {
        if (ritmoRespiratorio == null) {
            ritmoRespiratorio = new IntegerFilter();
        }
        return ritmoRespiratorio;
    }

    public void setRitmoRespiratorio(IntegerFilter ritmoRespiratorio) {
        this.ritmoRespiratorio = ritmoRespiratorio;
    }

    public IntegerFilter getOximetria() {
        return oximetria;
    }

    public IntegerFilter oximetria() {
        if (oximetria == null) {
            oximetria = new IntegerFilter();
        }
        return oximetria;
    }

    public void setOximetria(IntegerFilter oximetria) {
        this.oximetria = oximetria;
    }

    public IntegerFilter getPresionArterialSistolica() {
        return presionArterialSistolica;
    }

    public IntegerFilter presionArterialSistolica() {
        if (presionArterialSistolica == null) {
            presionArterialSistolica = new IntegerFilter();
        }
        return presionArterialSistolica;
    }

    public void setPresionArterialSistolica(IntegerFilter presionArterialSistolica) {
        this.presionArterialSistolica = presionArterialSistolica;
    }

    public IntegerFilter getPresionArterialDiastolica() {
        return presionArterialDiastolica;
    }

    public IntegerFilter presionArterialDiastolica() {
        if (presionArterialDiastolica == null) {
            presionArterialDiastolica = new IntegerFilter();
        }
        return presionArterialDiastolica;
    }

    public void setPresionArterialDiastolica(IntegerFilter presionArterialDiastolica) {
        this.presionArterialDiastolica = presionArterialDiastolica;
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

    public InstantFilter getFechaToma() {
        return fechaToma;
    }

    public InstantFilter fechaToma() {
        if (fechaToma == null) {
            fechaToma = new InstantFilter();
        }
        return fechaToma;
    }

    public void setFechaToma(InstantFilter fechaToma) {
        this.fechaToma = fechaToma;
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
        final Fisiometria1Criteria that = (Fisiometria1Criteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ritmoCardiaco, that.ritmoCardiaco) &&
            Objects.equals(ritmoRespiratorio, that.ritmoRespiratorio) &&
            Objects.equals(oximetria, that.oximetria) &&
            Objects.equals(presionArterialSistolica, that.presionArterialSistolica) &&
            Objects.equals(presionArterialDiastolica, that.presionArterialDiastolica) &&
            Objects.equals(temperatura, that.temperatura) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(fechaToma, that.fechaToma) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ritmoCardiaco,
            ritmoRespiratorio,
            oximetria,
            presionArterialSistolica,
            presionArterialDiastolica,
            temperatura,
            fechaRegistro,
            fechaToma,
            userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fisiometria1Criteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ritmoCardiaco != null ? "ritmoCardiaco=" + ritmoCardiaco + ", " : "") +
            (ritmoRespiratorio != null ? "ritmoRespiratorio=" + ritmoRespiratorio + ", " : "") +
            (oximetria != null ? "oximetria=" + oximetria + ", " : "") +
            (presionArterialSistolica != null ? "presionArterialSistolica=" + presionArterialSistolica + ", " : "") +
            (presionArterialDiastolica != null ? "presionArterialDiastolica=" + presionArterialDiastolica + ", " : "") +
            (temperatura != null ? "temperatura=" + temperatura + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (fechaToma != null ? "fechaToma=" + fechaToma + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
