package com.be4tech.becare3.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Agenda.
 */
@Entity
@Table(name = "agenda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "hora_medicamento")
    private Integer horaMedicamento;

    @ManyToOne
    private Medicamento medicamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getHoraMedicamento() {
        return this.horaMedicamento;
    }

    public Agenda horaMedicamento(Integer horaMedicamento) {
        this.horaMedicamento = horaMedicamento;
        return this;
    }

    public void setHoraMedicamento(Integer horaMedicamento) {
        this.horaMedicamento = horaMedicamento;
    }

    public Medicamento getMedicamento() {
        return this.medicamento;
    }

    public Agenda medicamento(Medicamento medicamento) {
        this.setMedicamento(medicamento);
        return this;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agenda)) {
            return false;
        }
        return id != null && id.equals(((Agenda) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agenda{" +
            "id=" + getId() +
            ", horaMedicamento=" + getHoraMedicamento() +
            "}";
    }
}
