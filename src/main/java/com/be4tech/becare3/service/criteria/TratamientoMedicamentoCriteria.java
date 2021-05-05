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
 * Criteria class for the {@link com.be4tech.becare3.domain.TratamientoMedicamento} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.TratamientoMedicamentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tratamiento-medicamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TratamientoMedicamentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dosis;

    private StringFilter intensidad;

    private LongFilter tratamietoId;

    private LongFilter medicamentoId;

    public TratamientoMedicamentoCriteria() {}

    public TratamientoMedicamentoCriteria(TratamientoMedicamentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dosis = other.dosis == null ? null : other.dosis.copy();
        this.intensidad = other.intensidad == null ? null : other.intensidad.copy();
        this.tratamietoId = other.tratamietoId == null ? null : other.tratamietoId.copy();
        this.medicamentoId = other.medicamentoId == null ? null : other.medicamentoId.copy();
    }

    @Override
    public TratamientoMedicamentoCriteria copy() {
        return new TratamientoMedicamentoCriteria(this);
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

    public StringFilter getDosis() {
        return dosis;
    }

    public StringFilter dosis() {
        if (dosis == null) {
            dosis = new StringFilter();
        }
        return dosis;
    }

    public void setDosis(StringFilter dosis) {
        this.dosis = dosis;
    }

    public StringFilter getIntensidad() {
        return intensidad;
    }

    public StringFilter intensidad() {
        if (intensidad == null) {
            intensidad = new StringFilter();
        }
        return intensidad;
    }

    public void setIntensidad(StringFilter intensidad) {
        this.intensidad = intensidad;
    }

    public LongFilter getTratamietoId() {
        return tratamietoId;
    }

    public LongFilter tratamietoId() {
        if (tratamietoId == null) {
            tratamietoId = new LongFilter();
        }
        return tratamietoId;
    }

    public void setTratamietoId(LongFilter tratamietoId) {
        this.tratamietoId = tratamietoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TratamientoMedicamentoCriteria that = (TratamientoMedicamentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dosis, that.dosis) &&
            Objects.equals(intensidad, that.intensidad) &&
            Objects.equals(tratamietoId, that.tratamietoId) &&
            Objects.equals(medicamentoId, that.medicamentoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dosis, intensidad, tratamietoId, medicamentoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TratamientoMedicamentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dosis != null ? "dosis=" + dosis + ", " : "") +
            (intensidad != null ? "intensidad=" + intensidad + ", " : "") +
            (tratamietoId != null ? "tratamietoId=" + tratamietoId + ", " : "") +
            (medicamentoId != null ? "medicamentoId=" + medicamentoId + ", " : "") +
            "}";
    }
}
