package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Encuesta;
import com.be4tech.becare3.repository.EncuestaRepository;
import com.be4tech.becare3.service.EncuestaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Encuesta}.
 */
@Service
@Transactional
public class EncuestaServiceImpl implements EncuestaService {

    private final Logger log = LoggerFactory.getLogger(EncuestaServiceImpl.class);

    private final EncuestaRepository encuestaRepository;

    public EncuestaServiceImpl(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    @Override
    public Encuesta save(Encuesta encuesta) {
        log.debug("Request to save Encuesta : {}", encuesta);
        return encuestaRepository.save(encuesta);
    }

    @Override
    public Optional<Encuesta> partialUpdate(Encuesta encuesta) {
        log.debug("Request to partially update Encuesta : {}", encuesta);

        return encuestaRepository
            .findById(encuesta.getId())
            .map(
                existingEncuesta -> {
                    if (encuesta.getFecha() != null) {
                        existingEncuesta.setFecha(encuesta.getFecha());
                    }
                    if (encuesta.getDebilidad() != null) {
                        existingEncuesta.setDebilidad(encuesta.getDebilidad());
                    }
                    if (encuesta.getCefalea() != null) {
                        existingEncuesta.setCefalea(encuesta.getCefalea());
                    }
                    if (encuesta.getCalambres() != null) {
                        existingEncuesta.setCalambres(encuesta.getCalambres());
                    }
                    if (encuesta.getNauseas() != null) {
                        existingEncuesta.setNauseas(encuesta.getNauseas());
                    }
                    if (encuesta.getVomito() != null) {
                        existingEncuesta.setVomito(encuesta.getVomito());
                    }
                    if (encuesta.getMareo() != null) {
                        existingEncuesta.setMareo(encuesta.getMareo());
                    }
                    if (encuesta.getNinguna() != null) {
                        existingEncuesta.setNinguna(encuesta.getNinguna());
                    }

                    return existingEncuesta;
                }
            )
            .map(encuestaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Encuesta> findAll(Pageable pageable) {
        log.debug("Request to get all Encuestas");
        return encuestaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Encuesta> findOne(Long id) {
        log.debug("Request to get Encuesta : {}", id);
        return encuestaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Encuesta : {}", id);
        encuestaRepository.deleteById(id);
    }
}
