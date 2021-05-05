package com.be4tech.becare3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CuestionarioEstado.
 */
@Entity
@Table(name = "cuestionario_estado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CuestionarioEstado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "valoracion")
    private String valoracion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "condicion" }, allowSetters = true)
    private Pregunta pregunta;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CuestionarioEstado id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getValor() {
        return this.valor;
    }

    public CuestionarioEstado valor(Integer valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getValoracion() {
        return this.valoracion;
    }

    public CuestionarioEstado valoracion(String valoracion) {
        this.valoracion = valoracion;
        return this;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public Pregunta getPregunta() {
        return this.pregunta;
    }

    public CuestionarioEstado pregunta(Pregunta pregunta) {
        this.setPregunta(pregunta);
        return this;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public User getUser() {
        return this.user;
    }

    public CuestionarioEstado user(User user) {
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
        if (!(o instanceof CuestionarioEstado)) {
            return false;
        }
        return id != null && id.equals(((CuestionarioEstado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuestionarioEstado{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", valoracion='" + getValoracion() + "'" +
            "}";
    }
}
