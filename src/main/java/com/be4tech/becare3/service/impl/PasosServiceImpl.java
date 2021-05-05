package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Pasos;
import com.be4tech.becare3.repository.PasosRepository;
import com.be4tech.becare3.service.PasosService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pasos}.
 */
@Service
@Transactional
public class PasosServiceImpl implements PasosService {

    private final Logger log = LoggerFactory.getLogger(PasosServiceImpl.class);

    private final PasosRepository pasosRepository;

    public PasosServiceImpl(PasosRepository pasosRepository) {
        this.pasosRepository = pasosRepository;
    }

    @Override
    public Pasos save(Pasos pasos) {
        log.debug("Request to save Pasos : {}", pasos);
        return pasosRepository.save(pasos);
    }

    @Override
    public Optional<Pasos> partialUpdate(Pasos pasos) {
        log.debug("Request to partially update Pasos : {}", pasos);

        return pasosRepository
            .findById(pasos.getId())
            .map(
                existingPasos -> {
                    if (pasos.getNroPasos() != null) {
                        existingPasos.setNroPasos(pasos.getNroPasos());
                    }
                    if (pasos.getTimeInstant() != null) {
                        existingPasos.setTimeInstant(pasos.getTimeInstant());
                    }

                    return existingPasos;
                }
            )
            .map(pasosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pasos> findAll(Pageable pageable) {
        log.debug("Request to get all Pasos");
        return pasosRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pasos> findOne(Long id) {
        log.debug("Request to get Pasos : {}", id);
        return pasosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pasos : {}", id);
        pasosRepository.deleteById(id);
    }
}
