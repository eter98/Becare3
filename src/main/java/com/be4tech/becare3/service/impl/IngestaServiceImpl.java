package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Ingesta;
import com.be4tech.becare3.repository.IngestaRepository;
import com.be4tech.becare3.service.IngestaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ingesta}.
 */
@Service
@Transactional
public class IngestaServiceImpl implements IngestaService {

    private final Logger log = LoggerFactory.getLogger(IngestaServiceImpl.class);

    private final IngestaRepository ingestaRepository;

    public IngestaServiceImpl(IngestaRepository ingestaRepository) {
        this.ingestaRepository = ingestaRepository;
    }

    @Override
    public Ingesta save(Ingesta ingesta) {
        log.debug("Request to save Ingesta : {}", ingesta);
        return ingestaRepository.save(ingesta);
    }

    @Override
    public Optional<Ingesta> partialUpdate(Ingesta ingesta) {
        log.debug("Request to partially update Ingesta : {}", ingesta);

        return ingestaRepository
            .findById(ingesta.getId())
            .map(
                existingIngesta -> {
                    if (ingesta.getTipo() != null) {
                        existingIngesta.setTipo(ingesta.getTipo());
                    }
                    if (ingesta.getConsumoCalorias() != null) {
                        existingIngesta.setConsumoCalorias(ingesta.getConsumoCalorias());
                    }
                    if (ingesta.getFechaRegistro() != null) {
                        existingIngesta.setFechaRegistro(ingesta.getFechaRegistro());
                    }

                    return existingIngesta;
                }
            )
            .map(ingestaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ingesta> findAll(Pageable pageable) {
        log.debug("Request to get all Ingestas");
        return ingestaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ingesta> findOne(Long id) {
        log.debug("Request to get Ingesta : {}", id);
        return ingestaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingesta : {}", id);
        ingestaRepository.deleteById(id);
    }
}
