package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Fisiometria1;
import com.be4tech.becare3.repository.Fisiometria1Repository;
import com.be4tech.becare3.service.Fisiometria1Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fisiometria1}.
 */
@Service
@Transactional
public class Fisiometria1ServiceImpl implements Fisiometria1Service {

    private final Logger log = LoggerFactory.getLogger(Fisiometria1ServiceImpl.class);

    private final Fisiometria1Repository fisiometria1Repository;

    public Fisiometria1ServiceImpl(Fisiometria1Repository fisiometria1Repository) {
        this.fisiometria1Repository = fisiometria1Repository;
    }

    @Override
    public Fisiometria1 save(Fisiometria1 fisiometria1) {
        log.debug("Request to save Fisiometria1 : {}", fisiometria1);
        return fisiometria1Repository.save(fisiometria1);
    }

    @Override
    public Optional<Fisiometria1> partialUpdate(Fisiometria1 fisiometria1) {
        log.debug("Request to partially update Fisiometria1 : {}", fisiometria1);

        return fisiometria1Repository
            .findById(fisiometria1.getId())
            .map(
                existingFisiometria1 -> {
                    if (fisiometria1.getRitmoCardiaco() != null) {
                        existingFisiometria1.setRitmoCardiaco(fisiometria1.getRitmoCardiaco());
                    }
                    if (fisiometria1.getRitmoRespiratorio() != null) {
                        existingFisiometria1.setRitmoRespiratorio(fisiometria1.getRitmoRespiratorio());
                    }
                    if (fisiometria1.getOximetria() != null) {
                        existingFisiometria1.setOximetria(fisiometria1.getOximetria());
                    }
                    if (fisiometria1.getPresionArterialSistolica() != null) {
                        existingFisiometria1.setPresionArterialSistolica(fisiometria1.getPresionArterialSistolica());
                    }
                    if (fisiometria1.getPresionArterialDiastolica() != null) {
                        existingFisiometria1.setPresionArterialDiastolica(fisiometria1.getPresionArterialDiastolica());
                    }
                    if (fisiometria1.getTemperatura() != null) {
                        existingFisiometria1.setTemperatura(fisiometria1.getTemperatura());
                    }
                    if (fisiometria1.getFechaRegistro() != null) {
                        existingFisiometria1.setFechaRegistro(fisiometria1.getFechaRegistro());
                    }
                    if (fisiometria1.getFechaToma() != null) {
                        existingFisiometria1.setFechaToma(fisiometria1.getFechaToma());
                    }

                    return existingFisiometria1;
                }
            )
            .map(fisiometria1Repository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Fisiometria1> findAll(Pageable pageable) {
        log.debug("Request to get all Fisiometria1s");
        return fisiometria1Repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fisiometria1> findOne(Long id) {
        log.debug("Request to get Fisiometria1 : {}", id);
        return fisiometria1Repository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fisiometria1 : {}", id);
        fisiometria1Repository.deleteById(id);
    }
}
