package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.PresionSanguinea;
import com.be4tech.becare3.repository.PresionSanguineaRepository;
import com.be4tech.becare3.service.PresionSanguineaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PresionSanguinea}.
 */
@Service
@Transactional
public class PresionSanguineaServiceImpl implements PresionSanguineaService {

    private final Logger log = LoggerFactory.getLogger(PresionSanguineaServiceImpl.class);

    private final PresionSanguineaRepository presionSanguineaRepository;

    public PresionSanguineaServiceImpl(PresionSanguineaRepository presionSanguineaRepository) {
        this.presionSanguineaRepository = presionSanguineaRepository;
    }

    @Override
    public PresionSanguinea save(PresionSanguinea presionSanguinea) {
        log.debug("Request to save PresionSanguinea : {}", presionSanguinea);
        return presionSanguineaRepository.save(presionSanguinea);
    }

    @Override
    public Optional<PresionSanguinea> partialUpdate(PresionSanguinea presionSanguinea) {
        log.debug("Request to partially update PresionSanguinea : {}", presionSanguinea);

        return presionSanguineaRepository
            .findById(presionSanguinea.getId())
            .map(
                existingPresionSanguinea -> {
                    if (presionSanguinea.getPresionSanguineaSistolica() != null) {
                        existingPresionSanguinea.setPresionSanguineaSistolica(presionSanguinea.getPresionSanguineaSistolica());
                    }
                    if (presionSanguinea.getPresionSanguineaDiastolica() != null) {
                        existingPresionSanguinea.setPresionSanguineaDiastolica(presionSanguinea.getPresionSanguineaDiastolica());
                    }
                    if (presionSanguinea.getFechaRegistro() != null) {
                        existingPresionSanguinea.setFechaRegistro(presionSanguinea.getFechaRegistro());
                    }

                    return existingPresionSanguinea;
                }
            )
            .map(presionSanguineaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PresionSanguinea> findAll(Pageable pageable) {
        log.debug("Request to get all PresionSanguineas");
        return presionSanguineaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PresionSanguinea> findOne(Long id) {
        log.debug("Request to get PresionSanguinea : {}", id);
        return presionSanguineaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PresionSanguinea : {}", id);
        presionSanguineaRepository.deleteById(id);
    }
}
