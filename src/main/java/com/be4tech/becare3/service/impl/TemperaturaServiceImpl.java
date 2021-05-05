package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Temperatura;
import com.be4tech.becare3.repository.TemperaturaRepository;
import com.be4tech.becare3.service.TemperaturaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Temperatura}.
 */
@Service
@Transactional
public class TemperaturaServiceImpl implements TemperaturaService {

    private final Logger log = LoggerFactory.getLogger(TemperaturaServiceImpl.class);

    private final TemperaturaRepository temperaturaRepository;

    public TemperaturaServiceImpl(TemperaturaRepository temperaturaRepository) {
        this.temperaturaRepository = temperaturaRepository;
    }

    @Override
    public Temperatura save(Temperatura temperatura) {
        log.debug("Request to save Temperatura : {}", temperatura);
        return temperaturaRepository.save(temperatura);
    }

    @Override
    public Optional<Temperatura> partialUpdate(Temperatura temperatura) {
        log.debug("Request to partially update Temperatura : {}", temperatura);

        return temperaturaRepository
            .findById(temperatura.getId())
            .map(
                existingTemperatura -> {
                    if (temperatura.getTemperatura() != null) {
                        existingTemperatura.setTemperatura(temperatura.getTemperatura());
                    }
                    if (temperatura.getFechaRegistro() != null) {
                        existingTemperatura.setFechaRegistro(temperatura.getFechaRegistro());
                    }

                    return existingTemperatura;
                }
            )
            .map(temperaturaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Temperatura> findAll(Pageable pageable) {
        log.debug("Request to get all Temperaturas");
        return temperaturaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Temperatura> findOne(Long id) {
        log.debug("Request to get Temperatura : {}", id);
        return temperaturaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Temperatura : {}", id);
        temperaturaRepository.deleteById(id);
    }
}
