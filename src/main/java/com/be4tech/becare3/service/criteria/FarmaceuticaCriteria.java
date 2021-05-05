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
 * Criteria class for the {@link com.be4tech.becare3.domain.Farmaceutica} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.FarmaceuticaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /farmaceuticas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FarmaceuticaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter direccion;

    private StringFilter propietario;

    public FarmaceuticaCriteria() {}

    public FarmaceuticaCriteria(FarmaceuticaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.propietario = other.propietario == null ? null : other.propietario.copy();
    }

    @Override
    public FarmaceuticaCriteria copy() {
        return new FarmaceuticaCriteria(this);
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

    public StringFilter getDireccion() {
        return direccion;
    }

    public StringFilter direccion() {
        if (direccion == null) {
            direccion = new StringFilter();
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public StringFilter getPropietario() {
        return propietario;
    }

    public StringFilter propietario() {
        if (propietario == null) {
            propietario = new StringFilter();
        }
        return propietario;
    }

    public void setPropietario(StringFilter propietario) {
        this.propietario = propietario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FarmaceuticaCriteria that = (FarmaceuticaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(propietario, that.propietario)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, direccion, propietario);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmaceuticaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (propietario != null ? "propietario=" + propietario + ", " : "") +
            "}";
    }
}
