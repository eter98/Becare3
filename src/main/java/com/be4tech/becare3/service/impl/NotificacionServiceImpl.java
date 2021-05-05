package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Notificacion;
import com.be4tech.becare3.repository.NotificacionRepository;
import com.be4tech.becare3.service.NotificacionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notificacion}.
 */
@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        log.debug("Request to save Notificacion : {}", notificacion);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Optional<Notificacion> partialUpdate(Notificacion notificacion) {
        log.debug("Request to partially update Notificacion : {}", notificacion);

        return notificacionRepository
            .findById(notificacion.getId())
            .map(
                existingNotificacion -> {
                    if (notificacion.getFechaInicio() != null) {
                        existingNotificacion.setFechaInicio(notificacion.getFechaInicio());
                    }
                    if (notificacion.getFechaActualizacion() != null) {
                        existingNotificacion.setFechaActualizacion(notificacion.getFechaActualizacion());
                    }
                    if (notificacion.getEstado() != null) {
                        existingNotificacion.setEstado(notificacion.getEstado());
                    }
                    if (notificacion.getTipoNotificacion() != null) {
                        existingNotificacion.setTipoNotificacion(notificacion.getTipoNotificacion());
                    }

                    return existingNotificacion;
                }
            )
            .map(notificacionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notificacion> findAll(Pageable pageable) {
        log.debug("Request to get all Notificacions");
        return notificacionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notificacion> findOne(Long id) {
        log.debug("Request to get Notificacion : {}", id);
        return notificacionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notificacion : {}", id);
        notificacionRepository.deleteById(id);
    }
}
