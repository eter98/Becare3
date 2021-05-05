package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fisiometria1.
 */
@Entity
@Table(name = "fisiometria_1")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fisiometria1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ritmo_cardiaco")
    private Integer ritmoCardiaco;

    @Column(name = "ritmo_respiratorio")
    private Integer ritmoRespiratorio;

    @Column(name = "oximetria")
    private Integer oximetria;

    @Column(name = "presion_arterial_sistolica")
    private Integer presionArterialSistolica;

    @Column(name = "presion_arterial_diastolica")
    private Integer presionArterialDiastolica;

    @Column(name = "temperatura")
    private Float temperatura;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @Column(name = "fecha_toma")
    private Instant fechaToma;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fisiometria1 id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRitmoCardiaco() {
        return this.ritmoCardiaco;
    }

    public Fisiometria1 ritmoCardiaco(Integer ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
        return this;
    }

    public void setRitmoCardiaco(Integer ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
    }

    public Integer getRitmoRespiratorio() {
        return this.ritmoRespiratorio;
    }

    public Fisiometria1 ritmoRespiratorio(Integer ritmoRespiratorio) {
        this.ritmoRespiratorio = ritmoRespiratorio;
        return this;
    }

    public void setRitmoRespiratorio(Integer ritmoRespiratorio) {
        this.ritmoRespiratorio = ritmoRespiratorio;
    }

    public Integer getOximetria() {
        return this.oximetria;
    }

    public Fisiometria1 oximetria(Integer oximetria) {
        this.oximetria = oximetria;
        return this;
    }

    public void setOximetria(Integer oximetria) {
        this.oximetria = oximetria;
    }

    public Integer getPresionArterialSistolica() {
        return this.presionArterialSistolica;
    }

    public Fisiometria1 presionArterialSistolica(Integer presionArterialSistolica) {
        this.presionArterialSistolica = presionArterialSistolica;
        return this;
    }

    public void setPresionArterialSistolica(Integer presionArterialSistolica) {
        this.presionArterialSistolica = presionArterialSistolica;
    }

    public Integer getPresionArterialDiastolica() {
        return this.presionArterialDiastolica;
    }

    public Fisiometria1 presionArterialDiastolica(Integer presionArterialDiastolica) {
        this.presionArterialDiastolica = presionArterialDiastolica;
        return this;
    }

    public void setPresionArterialDiastolica(Integer presionArterialDiastolica) {
        this.presionArterialDiastolica = presionArterialDiastolica;
    }

    public Float getTemperatura() {
        return this.temperatura;
    }

    public Fisiometria1 temperatura(Float temperatura) {
        this.temperatura = temperatura;
        return this;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Fisiometria1 fechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getFechaToma() {
        return this.fechaToma;
    }

    public Fisiometria1 fechaToma(Instant fechaToma) {
        this.fechaToma = fechaToma;
        return this;
    }

    public void setFechaToma(Instant fechaToma) {
        this.fechaToma = fechaToma;
    }

    public User getUser() {
        return this.user;
    }

    public Fisiometria1 user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fisiometria1)) {
            return false;
        }
        return id != null && id.equals(((Fisiometria1) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fisiometria1{" +
            "id=" + getId() +
            ", ritmoCardiaco=" + getRitmoCardiaco() +
            ", ritmoRespiratorio=" + getRitmoRespiratorio() +
            ", oximetria=" + getOximetria() +
            ", presionArterialSistolica=" + getPresionArterialSistolica() +
            ", presionArterialDiastolica=" + getPresionArterialDiastolica() +
            ", temperatura=" + getTemperatura() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", fechaToma='" + getFechaToma() + "'" +
            "}";
    }
}
