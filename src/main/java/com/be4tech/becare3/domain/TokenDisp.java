package com.be4tech.becare3.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TokenDisp.
 */
@Entity
@Table(name = "token_disp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TokenDisp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "token_conexion")
    private String tokenConexion;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private ZonedDateTime fechaFin;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TokenDisp id(Long id) {
        this.id = id;
        return this;
    }

    public String getTokenConexion() {
        return this.tokenConexion;
    }

    public TokenDisp tokenConexion(String tokenConexion) {
        this.tokenConexion = tokenConexion;
        return this;
    }

    public void setTokenConexion(String tokenConexion) {
        this.tokenConexion = tokenConexion;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public TokenDisp activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public TokenDisp fechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFechaFin() {
        return this.fechaFin;
    }

    public TokenDisp fechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public User getUser() {
        return this.user;
    }

    public TokenDisp user(User user) {
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
        if (!(o instanceof TokenDisp)) {
            return false;
        }
        return id != null && id.equals(((TokenDisp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TokenDisp{" +
            "id=" + getId() +
            ", tokenConexion='" + getTokenConexion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
