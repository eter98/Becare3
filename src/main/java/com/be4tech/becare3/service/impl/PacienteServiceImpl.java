package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Paciente;
import com.be4tech.becare3.repository.PacienteRepository;
import com.be4tech.becare3.service.PacienteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paciente}.
 */
@Service
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final Logger log = LoggerFactory.getLogger(PacienteServiceImpl.class);

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente save(Paciente paciente) {
        log.debug("Request to save Paciente : {}", paciente);
        return pacienteRepository.save(paciente);
    }

    @Override
    public Optional<Paciente> partialUpdate(Paciente paciente) {
        log.debug("Request to partially update Paciente : {}", paciente);

        return pacienteRepository
            .findById(paciente.getId())
            .map(
                existingPaciente -> {
                    if (paciente.getNombre() != null) {
                        existingPaciente.setNombre(paciente.getNombre());
                    }
                    if (paciente.getTipoIdentificacion() != null) {
                        existingPaciente.setTipoIdentificacion(paciente.getTipoIdentificacion());
                    }
                    if (paciente.getIdentificacion() != null) {
                        existingPaciente.setIdentificacion(paciente.getIdentificacion());
                    }
                    if (paciente.getEdad() != null) {
                        existingPaciente.setEdad(paciente.getEdad());
                    }
                    if (paciente.getSexo() != null) {
                        existingPaciente.setSexo(paciente.getSexo());
                    }
                    if (paciente.getPesoKG() != null) {
                        existingPaciente.setPesoKG(paciente.getPesoKG());
                    }
                    if (paciente.getEstaturaCM() != null) {
                        existingPaciente.setEstaturaCM(paciente.getEstaturaCM());
                    }
                    if (paciente.getOximetriaReferencia() != null) {
                        existingPaciente.setOximetriaReferencia(paciente.getOximetriaReferencia());
                    }
                    if (paciente.getTemperaturaReferencia() != null) {
                        existingPaciente.setTemperaturaReferencia(paciente.getTemperaturaReferencia());
                    }
                    if (paciente.getRitmoCardiacoReferencia() != null) {
                        existingPaciente.setRitmoCardiacoReferencia(paciente.getRitmoCardiacoReferencia());
                    }
                    if (paciente.getPresionSistolicaReferencia() != null) {
                        existingPaciente.setPresionSistolicaReferencia(paciente.getPresionSistolicaReferencia());
                    }
                    if (paciente.getPresionDistolicaReferencia() != null) {
                        existingPaciente.setPresionDistolicaReferencia(paciente.getPresionDistolicaReferencia());
                    }
                    if (paciente.getComentarios() != null) {
                        existingPaciente.setComentarios(paciente.getComentarios());
                    }
                    if (paciente.getPasosReferencia() != null) {
                        existingPaciente.setPasosReferencia(paciente.getPasosReferencia());
                    }
                    if (paciente.getCaloriasReferencia() != null) {
                        existingPaciente.setCaloriasReferencia(paciente.getCaloriasReferencia());
                    }
                    if (paciente.getMetaReferencia() != null) {
                        existingPaciente.setMetaReferencia(paciente.getMetaReferencia());
                    }

                    return existingPaciente;
                }
            )
            .map(pacienteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Paciente> findAll(Pageable pageable) {
        log.debug("Request to get all Pacientes");
        return pacienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> findOne(Long id) {
        log.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
