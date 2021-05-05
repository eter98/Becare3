package com.be4tech.becare3.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Condicion.
 */
@Entity
@Table(name = "condicion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Condicion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "condicion")
    private String condicion;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Condicion id(Long id) {
        this.id = id;
        return this;
    }

    public String getCondicion() {
        return this.condicion;
    }

    public Condicion condicion(String condicion) {
        this.condicion = condicion;
        return this;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Condicion descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Condicion)) {
            return false;
        }
        return id != null && id.equals(((Condicion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Condicion{" +
            "id=" + getId() +
            ", condicion='" + getCondicion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
