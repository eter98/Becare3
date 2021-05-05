package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Programa;
import com.be4tech.becare3.repository.ProgramaRepository;
import com.be4tech.becare3.service.ProgramaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Programa}.
 */
@Service
@Transactional
public class ProgramaServiceImpl implements ProgramaService {

    private final Logger log = LoggerFactory.getLogger(ProgramaServiceImpl.class);

    private final ProgramaRepository programaRepository;

    public ProgramaServiceImpl(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    @Override
    public Programa save(Programa programa) {
        log.debug("Request to save Programa : {}", programa);
        return programaRepository.save(programa);
    }

    @Override
    public Optional<Programa> partialUpdate(Programa programa) {
        log.debug("Request to partially update Programa : {}", programa);

        return programaRepository
            .findById(programa.getId())
            .map(
                existingPrograma -> {
                    if (programa.getCaloriasActividad() != null) {
                        existingPrograma.setCaloriasActividad(programa.getCaloriasActividad());
                    }
                    if (programa.getPasosActividad() != null) {
                        existingPrograma.setPasosActividad(programa.getPasosActividad());
                    }
                    if (programa.getFechaRegistro() != null) {
                        existingPrograma.setFechaRegistro(programa.getFechaRegistro());
                    }

                    return existingPrograma;
                }
            )
            .map(programaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Programa> findAll(Pageable pageable) {
        log.debug("Request to get all Programas");
        return programaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Programa> findOne(Long id) {
        log.debug("Request to get Programa : {}", id);
        return programaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Programa : {}", id);
        programaRepository.deleteById(id);
    }
}
