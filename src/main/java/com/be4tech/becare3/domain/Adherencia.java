package com.be4tech.becare3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Adherencia.
 */
@Entity
@Table(name = "adherencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Adherencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "hora_toma")
    private Instant horaToma;

    @Column(name = "respuesta")
    private Boolean respuesta;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    private Medicamento medicamento;

    @ManyToOne
    @JsonIgnoreProperties(value = { "condicion", "ips", "user", "tratamiento", "farmaceutica" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adherencia id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getHoraToma() {
        return this.horaToma;
    }

    public Adherencia horaToma(Instant horaToma) {
        this.horaToma = horaToma;
        return this;
    }

    public void setHoraToma(Instant horaToma) {
        this.horaToma = horaToma;
    }

    public Boolean getRespuesta() {
        return this.respuesta;
    }

    public Adherencia respuesta(Boolean respuesta) {
        this.respuesta = respuesta;
        return this;
    }

    public void setRespuesta(Boolean respuesta) {
        this.respuesta = respuesta;
    }

    public Integer getValor() {
        return this.valor;
    }

    public Adherencia valor(Integer valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Adherencia comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Medicamento getMedicamento() {
        return this.medicamento;
    }

    public Adherencia medicamento(Medicamento medicamento) {
        this.setMedicamento(medicamento);
        return this;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public Adherencia paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adherencia)) {
            return false;
        }
        return id != null && id.equals(((Adherencia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Adherencia{" +
            "id=" + getId() +
            ", horaToma='" + getHoraToma() + "'" +
            ", respuesta='" + getRespuesta() + "'" +
            ", valor=" + getValor() +
            ", comentario='" + getComentario() + "'" +
            "}";
    }
}
