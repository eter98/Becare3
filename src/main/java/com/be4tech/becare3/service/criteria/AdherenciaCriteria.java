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
 * Criteria class for the {@link com.be4tech.becare3.domain.Adherencia} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.AdherenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /adherencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdherenciaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter horaToma;

    private BooleanFilter respuesta;

    private IntegerFilter valor;

    private StringFilter comentario;

    private LongFilter medicamentoId;

    private LongFilter pacienteId;

    public AdherenciaCriteria() {}

    public AdherenciaCriteria(AdherenciaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.horaToma = other.horaToma == null ? null : other.horaToma.copy();
        this.respuesta = other.respuesta == null ? null : other.respuesta.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.comentario = other.comentario == null ? null : other.comentario.copy();
        this.medicamentoId = other.medicamentoId == null ? null : other.medicamentoId.copy();
        this.pacienteId = other.pacienteId == null ? null : other.pacienteId.copy();
    }

    @Override
    public AdherenciaCriteria copy() {
        return new AdherenciaCriteria(this);
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

    public InstantFilter getHoraToma() {
        return horaToma;
    }

    public InstantFilter horaToma() {
        if (horaToma == null) {
            horaToma = new InstantFilter();
        }
        return horaToma;
    }

    public void setHoraToma(InstantFilter horaToma) {
        this.horaToma = horaToma;
    }

    public BooleanFilter getRespuesta() {
        return respuesta;
    }

    public BooleanFilter respuesta() {
        if (respuesta == null) {
            respuesta = new BooleanFilter();
        }
        return respuesta;
    }

    public void setRespuesta(BooleanFilter respuesta) {
        this.respuesta = respuesta;
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

    public StringFilter getComentario() {
        return comentario;
    }

    public StringFilter comentario() {
        if (comentario == null) {
            comentario = new StringFilter();
        }
        return comentario;
    }

    public void setComentario(StringFilter comentario) {
        this.comentario = comentario;
    }

    public LongFilter getMedicamentoId() {
        return medicamentoId;
    }

    public LongFilter medicamentoId() {
        if (medicamentoId == null) {
            medicamentoId = new LongFilter();
        }
        return medicamentoId;
    }

    public void setMedicamentoId(LongFilter medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public LongFilter getPacienteId() {
        return pacienteId;
    }

    public LongFilter pacienteId() {
        if (pacienteId == null) {
            pacienteId = new LongFilter();
        }
        return pacienteId;
    }

    public void setPacienteId(LongFilter pacienteId) {
        this.pacienteId = pacienteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdherenciaCriteria that = (AdherenciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(horaToma, that.horaToma) &&
            Objects.equals(respuesta, that.respuesta) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(comentario, that.comentario) &&
            Objects.equals(medicamentoId, that.medicamentoId) &&
            Objects.equals(pacienteId, that.pacienteId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaToma, respuesta, valor, comentario, medicamentoId, pacienteId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdherenciaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (horaToma != null ? "horaToma=" + horaToma + ", " : "") +
            (respuesta != null ? "respuesta=" + respuesta + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (comentario != null ? "comentario=" + comentario + ", " : "") +
            (medicamentoId != null ? "medicamentoId=" + medicamentoId + ", " : "") +
            (pacienteId != null ? "pacienteId=" + pacienteId + ", " : "") +
            "}";
    }
}
