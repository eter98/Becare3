package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.TratamientoMedicamento;
import com.be4tech.becare3.repository.TratamientoMedicamentoRepository;
import com.be4tech.becare3.service.TratamientoMedicamentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TratamientoMedicamento}.
 */
@Service
@Transactional
public class TratamientoMedicamentoServiceImpl implements TratamientoMedicamentoService {

    private final Logger log = LoggerFactory.getLogger(TratamientoMedicamentoServiceImpl.class);

    private final TratamientoMedicamentoRepository tratamientoMedicamentoRepository;

    public TratamientoMedicamentoServiceImpl(TratamientoMedicamentoRepository tratamientoMedicamentoRepository) {
        this.tratamientoMedicamentoRepository = tratamientoMedicamentoRepository;
    }

    @Override
    public TratamientoMedicamento save(TratamientoMedicamento tratamientoMedicamento) {
        log.debug("Request to save TratamientoMedicamento : {}", tratamientoMedicamento);
        return tratamientoMedicamentoRepository.save(tratamientoMedicamento);
    }

    @Override
    public Optional<TratamientoMedicamento> partialUpdate(TratamientoMedicamento tratamientoMedicamento) {
        log.debug("Request to partially update TratamientoMedicamento : {}", tratamientoMedicamento);

        return tratamientoMedicamentoRepository
            .findById(tratamientoMedicamento.getId())
            .map(
                existingTratamientoMedicamento -> {
                    if (tratamientoMedicamento.getDosis() != null) {
                        existingTratamientoMedicamento.setDosis(tratamientoMedicamento.getDosis());
                    }
                    if (tratamientoMedicamento.getIntensidad() != null) {
                        existingTratamientoMedicamento.setIntensidad(tratamientoMedicamento.getIntensidad());
                    }

                    return existingTratamientoMedicamento;
                }
            )
            .map(tratamientoMedicamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TratamientoMedicamento> findAll(Pageable pageable) {
        log.debug("Request to get all TratamientoMedicamentos");
        return tratamientoMedicamentoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TratamientoMedicamento> findOne(Long id) {
        log.debug("Request to get TratamientoMedicamento : {}", id);
        return tratamientoMedicamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TratamientoMedicamento : {}", id);
        tratamientoMedicamentoRepository.deleteById(id);
    }
}
