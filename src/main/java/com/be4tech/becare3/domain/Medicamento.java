package com.be4tech.becare3.domain;

import com.be4tech.becare3.domain.enumeration.Presentacion;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Medicamento.
 */
@Entity
@Table(name = "medicamento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_ingreso")
    private Instant fechaIngreso;

    @Enumerated(EnumType.STRING)
    @Column(name = "presentacion")
    private Presentacion presentacion;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "generico")
    private String generico;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicamento id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Medicamento nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Medicamento descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaIngreso() {
        return this.fechaIngreso;
    }

    public Medicamento fechaIngreso(Instant fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
        return this;
    }

    public void setFechaIngreso(Instant fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Presentacion getPresentacion() {
        return this.presentacion;
    }

    public Medicamento presentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
        return this;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public String getGenerico() {
        return this.generico;
    }

    public Medicamento generico(String generico) {
        this.generico = generico;
        return this;
    }

    public void setGenerico(String generico) {
        this.generico = generico;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicamento)) {
            return false;
        }
        return id != null && id.equals(((Medicamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicamento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", presentacion='" + getPresentacion() + "'" +
            ", generico='" + getGenerico() + "'" +
            "}";
    }
}
