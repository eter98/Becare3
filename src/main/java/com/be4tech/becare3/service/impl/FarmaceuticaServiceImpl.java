package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Farmaceutica;
import com.be4tech.becare3.repository.FarmaceuticaRepository;
import com.be4tech.becare3.service.FarmaceuticaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Farmaceutica}.
 */
@Service
@Transactional
public class FarmaceuticaServiceImpl implements FarmaceuticaService {

    private final Logger log = LoggerFactory.getLogger(FarmaceuticaServiceImpl.class);

    private final FarmaceuticaRepository farmaceuticaRepository;

    public FarmaceuticaServiceImpl(FarmaceuticaRepository farmaceuticaRepository) {
        this.farmaceuticaRepository = farmaceuticaRepository;
    }

    @Override
    public Farmaceutica save(Farmaceutica farmaceutica) {
        log.debug("Request to save Farmaceutica : {}", farmaceutica);
        return farmaceuticaRepository.save(farmaceutica);
    }

    @Override
    public Optional<Farmaceutica> partialUpdate(Farmaceutica farmaceutica) {
        log.debug("Request to partially update Farmaceutica : {}", farmaceutica);

        return farmaceuticaRepository
            .findById(farmaceutica.getId())
            .map(
                existingFarmaceutica -> {
                    if (farmaceutica.getNombre() != null) {
                        existingFarmaceutica.setNombre(farmaceutica.getNombre());
                    }
                    if (farmaceutica.getDireccion() != null) {
                        existingFarmaceutica.setDireccion(farmaceutica.getDireccion());
                    }
                    if (farmaceutica.getPropietario() != null) {
                        existingFarmaceutica.setPropietario(farmaceutica.getPropietario());
                    }

                    return existingFarmaceutica;
                }
            )
            .map(farmaceuticaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Farmaceutica> findAll(Pageable pageable) {
        log.debug("Request to get all Farmaceuticas");
        return farmaceuticaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Farmaceutica> findOne(Long id) {
        log.debug("Request to get Farmaceutica : {}", id);
        return farmaceuticaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Farmaceutica : {}", id);
        farmaceuticaRepository.deleteById(id);
    }
}
