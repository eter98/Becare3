package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Peso;
import com.be4tech.becare3.repository.PesoRepository;
import com.be4tech.becare3.service.PesoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Peso}.
 */
@Service
@Transactional
public class PesoServiceImpl implements PesoService {

    private final Logger log = LoggerFactory.getLogger(PesoServiceImpl.class);

    private final PesoRepository pesoRepository;

    public PesoServiceImpl(PesoRepository pesoRepository) {
        this.pesoRepository = pesoRepository;
    }

    @Override
    public Peso save(Peso peso) {
        log.debug("Request to save Peso : {}", peso);
        return pesoRepository.save(peso);
    }

    @Override
    public Optional<Peso> partialUpdate(Peso peso) {
        log.debug("Request to partially update Peso : {}", peso);

        return pesoRepository
            .findById(peso.getId())
            .map(
                existingPeso -> {
                    if (peso.getPesoKG() != null) {
                        existingPeso.setPesoKG(peso.getPesoKG());
                    }
                    if (peso.getDescripcion() != null) {
                        existingPeso.setDescripcion(peso.getDescripcion());
                    }
                    if (peso.getFechaRegistro() != null) {
                        existingPeso.setFechaRegistro(peso.getFechaRegistro());
                    }

                    return existingPeso;
                }
            )
            .map(pesoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Peso> findAll(Pageable pageable) {
        log.debug("Request to get all Pesos");
        return pesoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Peso> findOne(Long id) {
        log.debug("Request to get Peso : {}", id);
        return pesoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Peso : {}", id);
        pesoRepository.deleteById(id);
    }
}
