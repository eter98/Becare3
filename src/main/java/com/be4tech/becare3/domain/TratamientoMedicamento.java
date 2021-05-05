package com.be4tech.becare3.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TratamientoMedicamento.
 */
@Entity
@Table(name = "tratamiento_medicamento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TratamientoMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dosis")
    private String dosis;

    @Column(name = "intensidad")
    private String intensidad;

    @ManyToOne
    private Tratamieto tratamieto;

    @ManyToOne
    private Medicamento medicamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TratamientoMedicamento id(Long id) {
        this.id = id;
        return this;
    }

    public String getDosis() {
        return this.dosis;
    }

    public TratamientoMedicamento dosis(String dosis) {
        this.dosis = dosis;
        return this;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getIntensidad() {
        return this.intensidad;
    }

    public TratamientoMedicamento intensidad(String intensidad) {
        this.intensidad = intensidad;
        return this;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public Tratamieto getTratamieto() {
        return this.tratamieto;
    }

    public TratamientoMedicamento tratamieto(Tratamieto tratamieto) {
        this.setTratamieto(tratamieto);
        return this;
    }

    public void setTratamieto(Tratamieto tratamieto) {
        this.tratamieto = tratamieto;
    }

    public Medicamento getMedicamento() {
        return this.medicamento;
    }

    public TratamientoMedicamento medicamento(Medicamento medicamento) {
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
        if (!(o instanceof TratamientoMedicamento)) {
            return false;
        }
        return id != null && id.equals(((TratamientoMedicamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TratamientoMedicamento{" +
            "id=" + getId() +
            ", dosis='" + getDosis() + "'" +
            ", intensidad='" + getIntensidad() + "'" +
            "}";
    }
}
