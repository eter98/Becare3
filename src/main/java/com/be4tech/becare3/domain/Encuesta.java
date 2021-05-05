package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Encuesta.
 */
@Entity
@Table(name = "encuesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Encuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "debilidad")
    private Boolean debilidad;

    @Column(name = "cefalea")
    private Boolean cefalea;

    @Column(name = "calambres")
    private Boolean calambres;

    @Column(name = "nauseas")
    private Boolean nauseas;

    @Column(name = "vomito")
    private Boolean vomito;

    @Column(name = "mareo")
    private Boolean mareo;

    @Column(name = "ninguna")
    private Boolean ninguna;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Encuesta id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Encuesta fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Boolean getDebilidad() {
        return this.debilidad;
    }

    public Encuesta debilidad(Boolean debilidad) {
        this.debilidad = debilidad;
        return this;
    }

    public void setDebilidad(Boolean debilidad) {
        this.debilidad = debilidad;
    }

    public Boolean getCefalea() {
        return this.cefalea;
    }

    public Encuesta cefalea(Boolean cefalea) {
        this.cefalea = cefalea;
        return this;
    }

    public void setCefalea(Boolean cefalea) {
        this.cefalea = cefalea;
    }

    public Boolean getCalambres() {
        return this.calambres;
    }

    public Encuesta calambres(Boolean calambres) {
        this.calambres = calambres;
        return this;
    }

    public void setCalambres(Boolean calambres) {
        this.calambres = calambres;
    }

    public Boolean getNauseas() {
        return this.nauseas;
    }

    public Encuesta nauseas(Boolean nauseas) {
        this.nauseas = nauseas;
        return this;
    }

    public void setNauseas(Boolean nauseas) {
        this.nauseas = nauseas;
    }

    public Boolean getVomito() {
        return this.vomito;
    }

    public Encuesta vomito(Boolean vomito) {
        this.vomito = vomito;
        return this;
    }

    public void setVomito(Boolean vomito) {
        this.vomito = vomito;
    }

    public Boolean getMareo() {
        return this.mareo;
    }

    public Encuesta mareo(Boolean mareo) {
        this.mareo = mareo;
        return this;
    }

    public void setMareo(Boolean mareo) {
        this.mareo = mareo;
    }

    public Boolean getNinguna() {
        return this.ninguna;
    }

    public Encuesta ninguna(Boolean ninguna) {
        this.ninguna = ninguna;
        return this;
    }

    public void setNinguna(Boolean ninguna) {
        this.ninguna = ninguna;
    }

    public User getUser() {
        return this.user;
    }

    public Encuesta user(User user) {
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
        if (!(o instanceof Encuesta)) {
            return false;
        }
        return id != null && id.equals(((Encuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Encuesta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", debilidad='" + getDebilidad() + "'" +
            ", cefalea='" + getCefalea() + "'" +
            ", calambres='" + getCalambres() + "'" +
            ", nauseas='" + getNauseas() + "'" +
            ", vomito='" + getVomito() + "'" +
            ", mareo='" + getMareo() + "'" +
            ", ninguna='" + getNinguna() + "'" +
            "}";
    }
}
