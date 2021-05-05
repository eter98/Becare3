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
 * Criteria class for the {@link com.be4tech.becare3.domain.Dispositivo} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.DispositivoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dispositivos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DispositivoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dispositivo;

    private StringFilter mac;

    private BooleanFilter conectado;

    private StringFilter userId;

    public DispositivoCriteria() {}

    public DispositivoCriteria(DispositivoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dispositivo = other.dispositivo == null ? null : other.dispositivo.copy();
        this.mac = other.mac == null ? null : other.mac.copy();
        this.conectado = other.conectado == null ? null : other.conectado.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public DispositivoCriteria copy() {
        return new DispositivoCriteria(this);
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

    public StringFilter getDispositivo() {
        return dispositivo;
    }

    public StringFilter dispositivo() {
        if (dispositivo == null) {
            dispositivo = new StringFilter();
        }
        return dispositivo;
    }

    public void setDispositivo(StringFilter dispositivo) {
        this.dispositivo = dispositivo;
    }

    public StringFilter getMac() {
        return mac;
    }

    public StringFilter mac() {
        if (mac == null) {
            mac = new StringFilter();
        }
        return mac;
    }

    public void setMac(StringFilter mac) {
        this.mac = mac;
    }

    public BooleanFilter getConectado() {
        return conectado;
    }

    public BooleanFilter conectado() {
        if (conectado == null) {
            conectado = new BooleanFilter();
        }
        return conectado;
    }

    public void setConectado(BooleanFilter conectado) {
        this.conectado = conectado;
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
        final DispositivoCriteria that = (DispositivoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dispositivo, that.dispositivo) &&
            Objects.equals(mac, that.mac) &&
            Objects.equals(conectado, that.conectado) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dispositivo, mac, conectado, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispositivoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dispositivo != null ? "dispositivo=" + dispositivo + ", " : "") +
            (mac != null ? "mac=" + mac + ", " : "") +
            (conectado != null ? "conectado=" + conectado + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
