package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Caloria;
import com.be4tech.becare3.repository.CaloriaRepository;
import com.be4tech.becare3.service.CaloriaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caloria}.
 */
@Service
@Transactional
public class CaloriaServiceImpl implements CaloriaService {

    private final Logger log = LoggerFactory.getLogger(CaloriaServiceImpl.class);

    private final CaloriaRepository caloriaRepository;

    public CaloriaServiceImpl(CaloriaRepository caloriaRepository) {
        this.caloriaRepository = caloriaRepository;
    }

    @Override
    public Caloria save(Caloria caloria) {
        log.debug("Request to save Caloria : {}", caloria);
        return caloriaRepository.save(caloria);
    }

    @Override
    public Optional<Caloria> partialUpdate(Caloria caloria) {
        log.debug("Request to partially update Caloria : {}", caloria);

        return caloriaRepository
            .findById(caloria.getId())
            .map(
                existingCaloria -> {
                    if (caloria.getCaloriasActivas() != null) {
                        existingCaloria.setCaloriasActivas(caloria.getCaloriasActivas());
                    }
                    if (caloria.getDescripcion() != null) {
                        existingCaloria.setDescripcion(caloria.getDescripcion());
                    }
                    if (caloria.getFechaRegistro() != null) {
                        existingCaloria.setFechaRegistro(caloria.getFechaRegistro());
                    }

                    return existingCaloria;
                }
            )
            .map(caloriaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Caloria> findAll(Pageable pageable) {
        log.debug("Request to get all Calorias");
        return caloriaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caloria> findOne(Long id) {
        log.debug("Request to get Caloria : {}", id);
        return caloriaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caloria : {}", id);
        caloriaRepository.deleteById(id);
    }
}
