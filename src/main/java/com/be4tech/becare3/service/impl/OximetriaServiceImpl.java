package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Oximetria;
import com.be4tech.becare3.repository.OximetriaRepository;
import com.be4tech.becare3.service.OximetriaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Oximetria}.
 */
@Service
@Transactional
public class OximetriaServiceImpl implements OximetriaService {

    private final Logger log = LoggerFactory.getLogger(OximetriaServiceImpl.class);

    private final OximetriaRepository oximetriaRepository;

    public OximetriaServiceImpl(OximetriaRepository oximetriaRepository) {
        this.oximetriaRepository = oximetriaRepository;
    }

    @Override
    public Oximetria save(Oximetria oximetria) {
        log.debug("Request to save Oximetria : {}", oximetria);
        return oximetriaRepository.save(oximetria);
    }

    @Override
    public Optional<Oximetria> partialUpdate(Oximetria oximetria) {
        log.debug("Request to partially update Oximetria : {}", oximetria);

        return oximetriaRepository
            .findById(oximetria.getId())
            .map(
                existingOximetria -> {
                    if (oximetria.getOximetria() != null) {
                        existingOximetria.setOximetria(oximetria.getOximetria());
                    }
                    if (oximetria.getFechaRegistro() != null) {
                        existingOximetria.setFechaRegistro(oximetria.getFechaRegistro());
                    }

                    return existingOximetria;
                }
            )
            .map(oximetriaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Oximetria> findAll(Pageable pageable) {
        log.debug("Request to get all Oximetrias");
        return oximetriaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Oximetria> findOne(Long id) {
        log.debug("Request to get Oximetria : {}", id);
        return oximetriaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Oximetria : {}", id);
        oximetriaRepository.deleteById(id);
    }
}
