package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Agenda;
import com.be4tech.becare3.repository.AgendaRepository;
import com.be4tech.becare3.service.AgendaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Agenda}.
 */
@Service
@Transactional
public class AgendaServiceImpl implements AgendaService {

    private final Logger log = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AgendaRepository agendaRepository;

    public AgendaServiceImpl(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public Agenda save(Agenda agenda) {
        log.debug("Request to save Agenda : {}", agenda);
        return agendaRepository.save(agenda);
    }

    @Override
    public Optional<Agenda> partialUpdate(Agenda agenda) {
        log.debug("Request to partially update Agenda : {}", agenda);

        return agendaRepository
            .findById(agenda.getId())
            .map(
                existingAgenda -> {
                    if (agenda.getHoraMedicamento() != null) {
                        existingAgenda.setHoraMedicamento(agenda.getHoraMedicamento());
                    }

                    return existingAgenda;
                }
            )
            .map(agendaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Agenda> findAll(Pageable pageable) {
        log.debug("Request to get all Agenda");
        return agendaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agenda> findOne(Long id) {
        log.debug("Request to get Agenda : {}", id);
        return agendaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agenda : {}", id);
        agendaRepository.deleteById(id);
    }
}
