package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Dispositivo;
import com.be4tech.becare3.repository.DispositivoRepository;
import com.be4tech.becare3.service.DispositivoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dispositivo}.
 */
@Service
@Transactional
public class DispositivoServiceImpl implements DispositivoService {

    private final Logger log = LoggerFactory.getLogger(DispositivoServiceImpl.class);

    private final DispositivoRepository dispositivoRepository;

    public DispositivoServiceImpl(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        log.debug("Request to save Dispositivo : {}", dispositivo);
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Optional<Dispositivo> partialUpdate(Dispositivo dispositivo) {
        log.debug("Request to partially update Dispositivo : {}", dispositivo);

        return dispositivoRepository
            .findById(dispositivo.getId())
            .map(
                existingDispositivo -> {
                    if (dispositivo.getDispositivo() != null) {
                        existingDispositivo.setDispositivo(dispositivo.getDispositivo());
                    }
                    if (dispositivo.getMac() != null) {
                        existingDispositivo.setMac(dispositivo.getMac());
                    }
                    if (dispositivo.getConectado() != null) {
                        existingDispositivo.setConectado(dispositivo.getConectado());
                    }

                    return existingDispositivo;
                }
            )
            .map(dispositivoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dispositivo> findAll(Pageable pageable) {
        log.debug("Request to get all Dispositivos");
        return dispositivoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dispositivo> findOne(Long id) {
        log.debug("Request to get Dispositivo : {}", id);
        return dispositivoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dispositivo : {}", id);
        dispositivoRepository.deleteById(id);
    }
}
