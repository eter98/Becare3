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
 * Criteria class for the {@link com.be4tech.becare3.domain.IPS} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.IPSResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IPSCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter nit;

    private StringFilter direccion;

    private StringFilter telefono;

    private StringFilter correoElectronico;

    public IPSCriteria() {}

    public IPSCriteria(IPSCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.nit = other.nit == null ? null : other.nit.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.correoElectronico = other.correoElectronico == null ? null : other.correoElectronico.copy();
    }

    @Override
    public IPSCriteria copy() {
        return new IPSCriteria(this);
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

    public StringFilter getNit() {
        return nit;
    }

    public StringFilter nit() {
        if (nit == null) {
            nit = new StringFilter();
        }
        return nit;
    }

    public void setNit(StringFilter nit) {
        this.nit = nit;
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

    public StringFilter getTelefono() {
        return telefono;
    }

    public StringFilter telefono() {
        if (telefono == null) {
            telefono = new StringFilter();
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getCorreoElectronico() {
        return correoElectronico;
    }

    public StringFilter correoElectronico() {
        if (correoElectronico == null) {
            correoElectronico = new StringFilter();
        }
        return correoElectronico;
    }

    public void setCorreoElectronico(StringFilter correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IPSCriteria that = (IPSCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(correoElectronico, that.correoElectronico)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, nit, direccion, telefono, correoElectronico);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IPSCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (nit != null ? "nit=" + nit + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (correoElectronico != null ? "correoElectronico=" + correoElectronico + ", " : "") +
            "}";
    }
}
