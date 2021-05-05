package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Alarma;
import com.be4tech.becare3.repository.AlarmaRepository;
import com.be4tech.becare3.service.AlarmaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Alarma}.
 */
@Service
@Transactional
public class AlarmaServiceImpl implements AlarmaService {

    private final Logger log = LoggerFactory.getLogger(AlarmaServiceImpl.class);

    private final AlarmaRepository alarmaRepository;

    public AlarmaServiceImpl(AlarmaRepository alarmaRepository) {
        this.alarmaRepository = alarmaRepository;
    }

    @Override
    public Alarma save(Alarma alarma) {
        log.debug("Request to save Alarma : {}", alarma);
        return alarmaRepository.save(alarma);
    }

    @Override
    public Optional<Alarma> partialUpdate(Alarma alarma) {
        log.debug("Request to partially update Alarma : {}", alarma);

        return alarmaRepository
            .findById(alarma.getId())
            .map(
                existingAlarma -> {
                    if (alarma.getTimeInstant() != null) {
                        existingAlarma.setTimeInstant(alarma.getTimeInstant());
                    }
                    if (alarma.getDescripcion() != null) {
                        existingAlarma.setDescripcion(alarma.getDescripcion());
                    }
                    if (alarma.getProcedimiento() != null) {
                        existingAlarma.setProcedimiento(alarma.getProcedimiento());
                    }
                    if (alarma.getTitulo() != null) {
                        existingAlarma.setTitulo(alarma.getTitulo());
                    }
                    if (alarma.getVerificar() != null) {
                        existingAlarma.setVerificar(alarma.getVerificar());
                    }
                    if (alarma.getObservaciones() != null) {
                        existingAlarma.setObservaciones(alarma.getObservaciones());
                    }
                    if (alarma.getPrioridad() != null) {
                        existingAlarma.setPrioridad(alarma.getPrioridad());
                    }

                    return existingAlarma;
                }
            )
            .map(alarmaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Alarma> findAll(Pageable pageable) {
        log.debug("Request to get all Alarmas");
        return alarmaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alarma> findOne(Long id) {
        log.debug("Request to get Alarma : {}", id);
        return alarmaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alarma : {}", id);
        alarmaRepository.deleteById(id);
    }
}
