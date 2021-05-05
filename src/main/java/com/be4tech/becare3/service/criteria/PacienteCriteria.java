package com.be4tech.becare3.service.criteria;

import com.be4tech.becare3.domain.enumeration.Identificaciont;
import com.be4tech.becare3.domain.enumeration.Sexop;
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
 * Criteria class for the {@link com.be4tech.becare3.domain.Paciente} entity. This class is used
 * in {@link com.be4tech.becare3.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PacienteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Identificaciont
     */
    public static class IdentificaciontFilter extends Filter<Identificaciont> {

        public IdentificaciontFilter() {}

        public IdentificaciontFilter(IdentificaciontFilter filter) {
            super(filter);
        }

        @Override
        public IdentificaciontFilter copy() {
            return new IdentificaciontFilter(this);
        }
    }

    /**
     * Class for filtering Sexop
     */
    public static class SexopFilter extends Filter<Sexop> {

        public SexopFilter() {}

        public SexopFilter(SexopFilter filter) {
            super(filter);
        }

        @Override
        public SexopFilter copy() {
            return new SexopFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private IdentificaciontFilter tipoIdentificacion;

    private IntegerFilter identificacion;

    private IntegerFilter edad;

    private SexopFilter sexo;

    private FloatFilter pesoKG;

    private IntegerFilter estaturaCM;

    private IntegerFilter oximetriaReferencia;

    private FloatFilter temperaturaReferencia;

    private IntegerFilter ritmoCardiacoReferencia;

    private IntegerFilter presionSistolicaReferencia;

    private IntegerFilter presionDistolicaReferencia;

    private IntegerFilter pasosReferencia;

    private IntegerFilter caloriasReferencia;

    private StringFilter metaReferencia;

    private LongFilter condicionId;

    private LongFilter ipsId;

    private StringFilter userId;

    private LongFilter tratamientoId;

    private LongFilter farmaceuticaId;

    public PacienteCriteria() {}

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.tipoIdentificacion = other.tipoIdentificacion == null ? null : other.tipoIdentificacion.copy();
        this.identificacion = other.identificacion == null ? null : other.identificacion.copy();
        this.edad = other.edad == null ? null : other.edad.copy();
        this.sexo = other.sexo == null ? null : other.sexo.copy();
        this.pesoKG = other.pesoKG == null ? null : other.pesoKG.copy();
        this.estaturaCM = other.estaturaCM == null ? null : other.estaturaCM.copy();
        this.oximetriaReferencia = other.oximetriaReferencia == null ? null : other.oximetriaReferencia.copy();
        this.temperaturaReferencia = other.temperaturaReferencia == null ? null : other.temperaturaReferencia.copy();
        this.ritmoCardiacoReferencia = other.ritmoCardiacoReferencia == null ? null : other.ritmoCardiacoReferencia.copy();
        this.presionSistolicaReferencia = other.presionSistolicaReferencia == null ? null : other.presionSistolicaReferencia.copy();
        this.presionDistolicaReferencia = other.presionDistolicaReferencia == null ? null : other.presionDistolicaReferencia.copy();
        this.pasosReferencia = other.pasosReferencia == null ? null : other.pasosReferencia.copy();
        this.caloriasReferencia = other.caloriasReferencia == null ? null : other.caloriasReferencia.copy();
        this.metaReferencia = other.metaReferencia == null ? null : other.metaReferencia.copy();
        this.condicionId = other.condicionId == null ? null : other.condicionId.copy();
        this.ipsId = other.ipsId == null ? null : other.ipsId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.tratamientoId = other.tratamientoId == null ? null : other.tratamientoId.copy();
        this.farmaceuticaId = other.farmaceuticaId == null ? null : other.farmaceuticaId.copy();
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
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

    public IdentificaciontFilter getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public IdentificaciontFilter tipoIdentificacion() {
        if (tipoIdentificacion == null) {
            tipoIdentificacion = new IdentificaciontFilter();
        }
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(IdentificaciontFilter tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public IntegerFilter getIdentificacion() {
        return identificacion;
    }

    public IntegerFilter identificacion() {
        if (identificacion == null) {
            identificacion = new IntegerFilter();
        }
        return identificacion;
    }

    public void setIdentificacion(IntegerFilter identificacion) {
        this.identificacion = identificacion;
    }

    public IntegerFilter getEdad() {
        return edad;
    }

    public IntegerFilter edad() {
        if (edad == null) {
            edad = new IntegerFilter();
        }
        return edad;
    }

    public void setEdad(IntegerFilter edad) {
        this.edad = edad;
    }

    public SexopFilter getSexo() {
        return sexo;
    }

    public SexopFilter sexo() {
        if (sexo == null) {
            sexo = new SexopFilter();
        }
        return sexo;
    }

    public void setSexo(SexopFilter sexo) {
        this.sexo = sexo;
    }

    public FloatFilter getPesoKG() {
        return pesoKG;
    }

    public FloatFilter pesoKG() {
        if (pesoKG == null) {
            pesoKG = new FloatFilter();
        }
        return pesoKG;
    }

    public void setPesoKG(FloatFilter pesoKG) {
        this.pesoKG = pesoKG;
    }

    public IntegerFilter getEstaturaCM() {
        return estaturaCM;
    }

    public IntegerFilter estaturaCM() {
        if (estaturaCM == null) {
            estaturaCM = new IntegerFilter();
        }
        return estaturaCM;
    }

    public void setEstaturaCM(IntegerFilter estaturaCM) {
        this.estaturaCM = estaturaCM;
    }

    public IntegerFilter getOximetriaReferencia() {
        return oximetriaReferencia;
    }

    public IntegerFilter oximetriaReferencia() {
        if (oximetriaReferencia == null) {
            oximetriaReferencia = new IntegerFilter();
        }
        return oximetriaReferencia;
    }

    public void setOximetriaReferencia(IntegerFilter oximetriaReferencia) {
        this.oximetriaReferencia = oximetriaReferencia;
    }

    public FloatFilter getTemperaturaReferencia() {
        return temperaturaReferencia;
    }

    public FloatFilter temperaturaReferencia() {
        if (temperaturaReferencia == null) {
            temperaturaReferencia = new FloatFilter();
        }
        return temperaturaReferencia;
    }

    public void setTemperaturaReferencia(FloatFilter temperaturaReferencia) {
        this.temperaturaReferencia = temperaturaReferencia;
    }

    public IntegerFilter getRitmoCardiacoReferencia() {
        return ritmoCardiacoReferencia;
    }

    public IntegerFilter ritmoCardiacoReferencia() {
        if (ritmoCardiacoReferencia == null) {
            ritmoCardiacoReferencia = new IntegerFilter();
        }
        return ritmoCardiacoReferencia;
    }

    public void setRitmoCardiacoReferencia(IntegerFilter ritmoCardiacoReferencia) {
        this.ritmoCardiacoReferencia = ritmoCardiacoReferencia;
    }

    public IntegerFilter getPresionSistolicaReferencia() {
        return presionSistolicaReferencia;
    }

    public IntegerFilter presionSistolicaReferencia() {
        if (presionSistolicaReferencia == null) {
            presionSistolicaReferencia = new IntegerFilter();
        }
        return presionSistolicaReferencia;
    }

    public void setPresionSistolicaReferencia(IntegerFilter presionSistolicaReferencia) {
        this.presionSistolicaReferencia = presionSistolicaReferencia;
    }

    public IntegerFilter getPresionDistolicaReferencia() {
        return presionDistolicaReferencia;
    }

    public IntegerFilter presionDistolicaReferencia() {
        if (presionDistolicaReferencia == null) {
            presionDistolicaReferencia = new IntegerFilter();
        }
        return presionDistolicaReferencia;
    }

    public void setPresionDistolicaReferencia(IntegerFilter presionDistolicaReferencia) {
        this.presionDistolicaReferencia = presionDistolicaReferencia;
    }

    public IntegerFilter getPasosReferencia() {
        return pasosReferencia;
    }

    public IntegerFilter pasosReferencia() {
        if (pasosReferencia == null) {
            pasosReferencia = new IntegerFilter();
        }
        return pasosReferencia;
    }

    public void setPasosReferencia(IntegerFilter pasosReferencia) {
        this.pasosReferencia = pasosReferencia;
    }

    public IntegerFilter getCaloriasReferencia() {
        return caloriasReferencia;
    }

    public IntegerFilter caloriasReferencia() {
        if (caloriasReferencia == null) {
            caloriasReferencia = new IntegerFilter();
        }
        return caloriasReferencia;
    }

    public void setCaloriasReferencia(IntegerFilter caloriasReferencia) {
        this.caloriasReferencia = caloriasReferencia;
    }

    public StringFilter getMetaReferencia() {
        return metaReferencia;
    }

    public StringFilter metaReferencia() {
        if (metaReferencia == null) {
            metaReferencia = new StringFilter();
        }
        return metaReferencia;
    }

    public void setMetaReferencia(StringFilter metaReferencia) {
        this.metaReferencia = metaReferencia;
    }

    public LongFilter getCondicionId() {
        return condicionId;
    }

    public LongFilter condicionId() {
        if (condicionId == null) {
            condicionId = new LongFilter();
        }
        return condicionId;
    }

    public void setCondicionId(LongFilter condicionId) {
        this.condicionId = condicionId;
    }

    public LongFilter getIpsId() {
        return ipsId;
    }

    public LongFilter ipsId() {
        if (ipsId == null) {
            ipsId = new LongFilter();
        }
        return ipsId;
    }

    public void setIpsId(LongFilter ipsId) {
        this.ipsId = ipsId;
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

    public LongFilter getTratamientoId() {
        return tratamientoId;
    }

    public LongFilter tratamientoId() {
        if (tratamientoId == null) {
            tratamientoId = new LongFilter();
        }
        return tratamientoId;
    }

    public void setTratamientoId(LongFilter tratamientoId) {
        this.tratamientoId = tratamientoId;
    }

    public LongFilter getFarmaceuticaId() {
        return farmaceuticaId;
    }

    public LongFilter farmaceuticaId() {
        if (farmaceuticaId == null) {
            farmaceuticaId = new LongFilter();
        }
        return farmaceuticaId;
    }

    public void setFarmaceuticaId(LongFilter farmaceuticaId) {
        this.farmaceuticaId = farmaceuticaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PacienteCriteria that = (PacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(tipoIdentificacion, that.tipoIdentificacion) &&
            Objects.equals(identificacion, that.identificacion) &&
            Objects.equals(edad, that.edad) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(pesoKG, that.pesoKG) &&
            Objects.equals(estaturaCM, that.estaturaCM) &&
            Objects.equals(oximetriaReferencia, that.oximetriaReferencia) &&
            Objects.equals(temperaturaReferencia, that.temperaturaReferencia) &&
            Objects.equals(ritmoCardiacoReferencia, that.ritmoCardiacoReferencia) &&
            Objects.equals(presionSistolicaReferencia, that.presionSistolicaReferencia) &&
            Objects.equals(presionDistolicaReferencia, that.presionDistolicaReferencia) &&
            Objects.equals(pasosReferencia, that.pasosReferencia) &&
            Objects.equals(caloriasReferencia, that.caloriasReferencia) &&
            Objects.equals(metaReferencia, that.metaReferencia) &&
            Objects.equals(condicionId, that.condicionId) &&
            Objects.equals(ipsId, that.ipsId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(tratamientoId, that.tratamientoId) &&
            Objects.equals(farmaceuticaId, that.farmaceuticaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            tipoIdentificacion,
            identificacion,
            edad,
            sexo,
            pesoKG,
            estaturaCM,
            oximetriaReferencia,
            temperaturaReferencia,
            ritmoCardiacoReferencia,
            presionSistolicaReferencia,
            presionDistolicaReferencia,
            pasosReferencia,
            caloriasReferencia,
            metaReferencia,
            condicionId,
            ipsId,
            userId,
            tratamientoId,
            farmaceuticaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (tipoIdentificacion != null ? "tipoIdentificacion=" + tipoIdentificacion + ", " : "") +
            (identificacion != null ? "identificacion=" + identificacion + ", " : "") +
            (edad != null ? "edad=" + edad + ", " : "") +
            (sexo != null ? "sexo=" + sexo + ", " : "") +
            (pesoKG != null ? "pesoKG=" + pesoKG + ", " : "") +
            (estaturaCM != null ? "estaturaCM=" + estaturaCM + ", " : "") +
            (oximetriaReferencia != null ? "oximetriaReferencia=" + oximetriaReferencia + ", " : "") +
            (temperaturaReferencia != null ? "temperaturaReferencia=" + temperaturaReferencia + ", " : "") +
            (ritmoCardiacoReferencia != null ? "ritmoCardiacoReferencia=" + ritmoCardiacoReferencia + ", " : "") +
            (presionSistolicaReferencia != null ? "presionSistolicaReferencia=" + presionSistolicaReferencia + ", " : "") +
            (presionDistolicaReferencia != null ? "presionDistolicaReferencia=" + presionDistolicaReferencia + ", " : "") +
            (pasosReferencia != null ? "pasosReferencia=" + pasosReferencia + ", " : "") +
            (caloriasReferencia != null ? "caloriasReferencia=" + caloriasReferencia + ", " : "") +
            (metaReferencia != null ? "metaReferencia=" + metaReferencia + ", " : "") +
            (condicionId != null ? "condicionId=" + condicionId + ", " : "") +
            (ipsId != null ? "ipsId=" + ipsId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (tratamientoId != null ? "tratamientoId=" + tratamientoId + ", " : "") +
            (farmaceuticaId != null ? "farmaceuticaId=" + farmaceuticaId + ", " : "") +
            "}";
    }
}
