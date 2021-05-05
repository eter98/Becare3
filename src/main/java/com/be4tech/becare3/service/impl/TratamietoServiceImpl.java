package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.repository.TratamietoRepository;
import com.be4tech.becare3.service.TratamietoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tratamieto}.
 */
@Service
@Transactional
public class TratamietoServiceImpl implements TratamietoService {

    private final Logger log = LoggerFactory.getLogger(TratamietoServiceImpl.class);

    private final TratamietoRepository tratamietoRepository;

    public TratamietoServiceImpl(TratamietoRepository tratamietoRepository) {
        this.tratamietoRepository = tratamietoRepository;
    }

    @Override
    public Tratamieto save(Tratamieto tratamieto) {
        log.debug("Request to save Tratamieto : {}", tratamieto);
        return tratamietoRepository.save(tratamieto);
    }

    @Override
    public Optional<Tratamieto> partialUpdate(Tratamieto tratamieto) {
        log.debug("Request to partially update Tratamieto : {}", tratamieto);

        return tratamietoRepository
            .findById(tratamieto.getId())
            .map(
                existingTratamieto -> {
                    if (tratamieto.getDescripcionTratamiento() != null) {
                        existingTratamieto.setDescripcionTratamiento(tratamieto.getDescripcionTratamiento());
                    }
                    if (tratamieto.getFechaInicio() != null) {
                        existingTratamieto.setFechaInicio(tratamieto.getFechaInicio());
                    }
                    if (tratamieto.getFechaFin() != null) {
                        existingTratamieto.setFechaFin(tratamieto.getFechaFin());
                    }

                    return existingTratamieto;
                }
            )
            .map(tratamietoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tratamieto> findAll(Pageable pageable) {
        log.debug("Request to get all Tratamietos");
        return tratamietoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tratamieto> findOne(Long id) {
        log.debug("Request to get Tratamieto : {}", id);
        return tratamietoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tratamieto : {}", id);
        tratamietoRepository.deleteById(id);
    }
}
