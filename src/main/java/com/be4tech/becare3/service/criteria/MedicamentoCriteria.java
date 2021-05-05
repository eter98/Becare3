package com.be4tech.becare3.service.criteria;

import com.be4tech.becare3.domain.enumeration.Presentacion;
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
 * Criteria class for the {@link com.be4tech.becare3.domain.Medicamento} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.MedicamentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medicamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedicamentoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Presentacion
     */
    public static class PresentacionFilter extends Filter<Presentacion> {

        public PresentacionFilter() {}

        public PresentacionFilter(PresentacionFilter filter) {
            super(filter);
        }

        @Override
        public PresentacionFilter copy() {
            return new PresentacionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private InstantFilter fechaIngreso;

    private PresentacionFilter presentacion;

    public MedicamentoCriteria() {}

    public MedicamentoCriteria(MedicamentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.fechaIngreso = other.fechaIngreso == null ? null : other.fechaIngreso.copy();
        this.presentacion = other.presentacion == null ? null : other.presentacion.copy();
    }

    @Override
    public MedicamentoCriteria copy() {
        return new MedicamentoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public InstantFilter getFechaIngreso() {
        return fechaIngreso;
    }

    public InstantFilter fechaIngreso() {
        if (fechaIngreso == null) {
            fechaIngreso = new InstantFilter();
        }
        return fechaIngreso;
    }

    public void setFechaIngreso(InstantFilter fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public PresentacionFilter getPresentacion() {
        return presentacion;
    }

    public PresentacionFilter presentacion() {
        if (presentacion == null) {
            presentacion = new PresentacionFilter();
        }
        return presentacion;
    }

    public void setPresentacion(PresentacionFilter presentacion) {
        this.presentacion = presentacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicamentoCriteria that = (MedicamentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(fechaIngreso, that.fechaIngreso) &&
            Objects.equals(presentacion, that.presentacion)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, fechaIngreso, presentacion);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (fechaIngreso != null ? "fechaIngreso=" + fechaIngreso + ", " : "") +
            (presentacion != null ? "presentacion=" + presentacion + ", " : "") +
            "}";
    }
}
