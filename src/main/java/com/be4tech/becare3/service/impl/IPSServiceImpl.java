package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.IPS;
import com.be4tech.becare3.repository.IPSRepository;
import com.be4tech.becare3.service.IPSService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IPS}.
 */
@Service
@Transactional
public class IPSServiceImpl implements IPSService {

    private final Logger log = LoggerFactory.getLogger(IPSServiceImpl.class);

    private final IPSRepository iPSRepository;

    public IPSServiceImpl(IPSRepository iPSRepository) {
        this.iPSRepository = iPSRepository;
    }

    @Override
    public IPS save(IPS iPS) {
        log.debug("Request to save IPS : {}", iPS);
        return iPSRepository.save(iPS);
    }

    @Override
    public Optional<IPS> partialUpdate(IPS iPS) {
        log.debug("Request to partially update IPS : {}", iPS);

        return iPSRepository
            .findById(iPS.getId())
            .map(
                existingIPS -> {
                    if (iPS.getNombre() != null) {
                        existingIPS.setNombre(iPS.getNombre());
                    }
                    if (iPS.getNit() != null) {
                        existingIPS.setNit(iPS.getNit());
                    }
                    if (iPS.getDireccion() != null) {
                        existingIPS.setDireccion(iPS.getDireccion());
                    }
                    if (iPS.getTelefono() != null) {
                        existingIPS.setTelefono(iPS.getTelefono());
                    }
                    if (iPS.getCorreoElectronico() != null) {
                        existingIPS.setCorreoElectronico(iPS.getCorreoElectronico());
                    }

                    return existingIPS;
                }
            )
            .map(iPSRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IPS> findAll(Pageable pageable) {
        log.debug("Request to get all IPS");
        return iPSRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IPS> findOne(Long id) {
        log.debug("Request to get IPS : {}", id);
        return iPSRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IPS : {}", id);
        iPSRepository.deleteById(id);
    }
}
