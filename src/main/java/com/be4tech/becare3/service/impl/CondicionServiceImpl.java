package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.repository.CondicionRepository;
import com.be4tech.becare3.service.CondicionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Condicion}.
 */
@Service
@Transactional
public class CondicionServiceImpl implements CondicionService {

    private final Logger log = LoggerFactory.getLogger(CondicionServiceImpl.class);

    private final CondicionRepository condicionRepository;

    public CondicionServiceImpl(CondicionRepository condicionRepository) {
        this.condicionRepository = condicionRepository;
    }

    @Override
    public Condicion save(Condicion condicion) {
        log.debug("Request to save Condicion : {}", condicion);
        return condicionRepository.save(condicion);
    }

    @Override
    public Optional<Condicion> partialUpdate(Condicion condicion) {
        log.debug("Request to partially update Condicion : {}", condicion);

        return condicionRepository
            .findById(condicion.getId())
            .map(
                existingCondicion -> {
                    if (condicion.getCondicion() != null) {
                        existingCondicion.setCondicion(condicion.getCondicion());
                    }
                    if (condicion.getDescripcion() != null) {
                        existingCondicion.setDescripcion(condicion.getDescripcion());
                    }

                    return existingCondicion;
                }
            )
            .map(condicionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Condicion> findAll(Pageable pageable) {
        log.debug("Request to get all Condicions");
        return condicionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Condicion> findOne(Long id) {
        log.debug("Request to get Condicion : {}", id);
        return condicionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Condicion : {}", id);
        condicionRepository.deleteById(id);
    }
}
