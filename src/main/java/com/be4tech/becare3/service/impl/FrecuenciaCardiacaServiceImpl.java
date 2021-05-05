package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.FrecuenciaCardiaca;
import com.be4tech.becare3.repository.FrecuenciaCardiacaRepository;
import com.be4tech.becare3.service.FrecuenciaCardiacaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FrecuenciaCardiaca}.
 */
@Service
@Transactional
public class FrecuenciaCardiacaServiceImpl implements FrecuenciaCardiacaService {

    private final Logger log = LoggerFactory.getLogger(FrecuenciaCardiacaServiceImpl.class);

    private final FrecuenciaCardiacaRepository frecuenciaCardiacaRepository;

    public FrecuenciaCardiacaServiceImpl(FrecuenciaCardiacaRepository frecuenciaCardiacaRepository) {
        this.frecuenciaCardiacaRepository = frecuenciaCardiacaRepository;
    }

    @Override
    public FrecuenciaCardiaca save(FrecuenciaCardiaca frecuenciaCardiaca) {
        log.debug("Request to save FrecuenciaCardiaca : {}", frecuenciaCardiaca);
        return frecuenciaCardiacaRepository.save(frecuenciaCardiaca);
    }

    @Override
    public Optional<FrecuenciaCardiaca> partialUpdate(FrecuenciaCardiaca frecuenciaCardiaca) {
        log.debug("Request to partially update FrecuenciaCardiaca : {}", frecuenciaCardiaca);

        return frecuenciaCardiacaRepository
            .findById(frecuenciaCardiaca.getId())
            .map(
                existingFrecuenciaCardiaca -> {
                    if (frecuenciaCardiaca.getFrecuenciaCardiaca() != null) {
                        existingFrecuenciaCardiaca.setFrecuenciaCardiaca(frecuenciaCardiaca.getFrecuenciaCardiaca());
                    }
                    if (frecuenciaCardiaca.getFechaRegistro() != null) {
                        existingFrecuenciaCardiaca.setFechaRegistro(frecuenciaCardiaca.getFechaRegistro());
                    }

                    return existingFrecuenciaCardiaca;
                }
            )
            .map(frecuenciaCardiacaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrecuenciaCardiaca> findAll(Pageable pageable) {
        log.debug("Request to get all FrecuenciaCardiacas");
        return frecuenciaCardiacaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrecuenciaCardiaca> findOne(Long id) {
        log.debug("Request to get FrecuenciaCardiaca : {}", id);
        return frecuenciaCardiacaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrecuenciaCardiaca : {}", id);
        frecuenciaCardiacaRepository.deleteById(id);
    }
}
