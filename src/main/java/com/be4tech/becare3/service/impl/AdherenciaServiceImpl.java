package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Adherencia;
import com.be4tech.becare3.repository.AdherenciaRepository;
import com.be4tech.becare3.service.AdherenciaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Adherencia}.
 */
@Service
@Transactional
public class AdherenciaServiceImpl implements AdherenciaService {

    private final Logger log = LoggerFactory.getLogger(AdherenciaServiceImpl.class);

    private final AdherenciaRepository adherenciaRepository;

    public AdherenciaServiceImpl(AdherenciaRepository adherenciaRepository) {
        this.adherenciaRepository = adherenciaRepository;
    }

    @Override
    public Adherencia save(Adherencia adherencia) {
        log.debug("Request to save Adherencia : {}", adherencia);
        return adherenciaRepository.save(adherencia);
    }

    @Override
    public Optional<Adherencia> partialUpdate(Adherencia adherencia) {
        log.debug("Request to partially update Adherencia : {}", adherencia);

        return adherenciaRepository
            .findById(adherencia.getId())
            .map(
                existingAdherencia -> {
                    if (adherencia.getHoraToma() != null) {
                        existingAdherencia.setHoraToma(adherencia.getHoraToma());
                    }
                    if (adherencia.getRespuesta() != null) {
                        existingAdherencia.setRespuesta(adherencia.getRespuesta());
                    }
                    if (adherencia.getValor() != null) {
                        existingAdherencia.setValor(adherencia.getValor());
                    }
                    if (adherencia.getComentario() != null) {
                        existingAdherencia.setComentario(adherencia.getComentario());
                    }

                    return existingAdherencia;
                }
            )
            .map(adherenciaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Adherencia> findAll(Pageable pageable) {
        log.debug("Request to get all Adherencias");
        return adherenciaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Adherencia> findOne(Long id) {
        log.debug("Request to get Adherencia : {}", id);
        return adherenciaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Adherencia : {}", id);
        adherenciaRepository.deleteById(id);
    }
}
