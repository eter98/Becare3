package com.be4tech.becare3.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dispositivo.
 */
@Entity
@Table(name = "dispositivo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dispositivo")
    private String dispositivo;

    @Column(name = "mac")
    private String mac;

    @Column(name = "conectado")
    private Boolean conectado;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dispositivo id(Long id) {
        this.id = id;
        return this;
    }

    public String getDispositivo() {
        return this.dispositivo;
    }

    public Dispositivo dispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
        return this;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getMac() {
        return this.mac;
    }

    public Dispositivo mac(String mac) {
        this.mac = mac;
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Boolean getConectado() {
        return this.conectado;
    }

    public Dispositivo conectado(Boolean conectado) {
        this.conectado = conectado;
        return this;
    }

    public void setConectado(Boolean conectado) {
        this.conectado = conectado;
    }

    public User getUser() {
        return this.user;
    }

    public Dispositivo user(User user) {
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
        if (!(o instanceof Dispositivo)) {
            return false;
        }
        return id != null && id.equals(((Dispositivo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dispositivo{" +
            "id=" + getId() +
            ", dispositivo='" + getDispositivo() + "'" +
            ", mac='" + getMac() + "'" +
            ", conectado='" + getConectado() + "'" +
            "}";
    }
}
