package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Sueno;
import com.be4tech.becare3.repository.SuenoRepository;
import com.be4tech.becare3.service.SuenoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sueno}.
 */
@Service
@Transactional
public class SuenoServiceImpl implements SuenoService {

    private final Logger log = LoggerFactory.getLogger(SuenoServiceImpl.class);

    private final SuenoRepository suenoRepository;

    public SuenoServiceImpl(SuenoRepository suenoRepository) {
        this.suenoRepository = suenoRepository;
    }

    @Override
    public Sueno save(Sueno sueno) {
        log.debug("Request to save Sueno : {}", sueno);
        return suenoRepository.save(sueno);
    }

    @Override
    public Optional<Sueno> partialUpdate(Sueno sueno) {
        log.debug("Request to partially update Sueno : {}", sueno);

        return suenoRepository
            .findById(sueno.getId())
            .map(
                existingSueno -> {
                    if (sueno.getSuperficial() != null) {
                        existingSueno.setSuperficial(sueno.getSuperficial());
                    }
                    if (sueno.getProfundo() != null) {
                        existingSueno.setProfundo(sueno.getProfundo());
                    }
                    if (sueno.getDespierto() != null) {
                        existingSueno.setDespierto(sueno.getDespierto());
                    }
                    if (sueno.getTimeInstant() != null) {
                        existingSueno.setTimeInstant(sueno.getTimeInstant());
                    }

                    return existingSueno;
                }
            )
            .map(suenoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sueno> findAll(Pageable pageable) {
        log.debug("Request to get all Suenos");
        return suenoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sueno> findOne(Long id) {
        log.debug("Request to get Sueno : {}", id);
        return suenoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sueno : {}", id);
        suenoRepository.deleteById(id);
    }
}
