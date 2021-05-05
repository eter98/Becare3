package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Pregunta;
import com.be4tech.becare3.repository.PreguntaRepository;
import com.be4tech.becare3.service.PreguntaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pregunta}.
 */
@Service
@Transactional
public class PreguntaServiceImpl implements PreguntaService {

    private final Logger log = LoggerFactory.getLogger(PreguntaServiceImpl.class);

    private final PreguntaRepository preguntaRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Pregunta save(Pregunta pregunta) {
        log.debug("Request to save Pregunta : {}", pregunta);
        return preguntaRepository.save(pregunta);
    }

    @Override
    public Optional<Pregunta> partialUpdate(Pregunta pregunta) {
        log.debug("Request to partially update Pregunta : {}", pregunta);

        return preguntaRepository
            .findById(pregunta.getId())
            .map(
                existingPregunta -> {
                    if (pregunta.getPregunta() != null) {
                        existingPregunta.setPregunta(pregunta.getPregunta());
                    }

                    return existingPregunta;
                }
            )
            .map(preguntaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pregunta> findAll(Pageable pageable) {
        log.debug("Request to get all Preguntas");
        return preguntaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pregunta> findOne(Long id) {
        log.debug("Request to get Pregunta : {}", id);
        return preguntaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pregunta : {}", id);
        preguntaRepository.deleteById(id);
    }
}
