package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.CuestionarioEstado;
import com.be4tech.becare3.repository.CuestionarioEstadoRepository;
import com.be4tech.becare3.service.CuestionarioEstadoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CuestionarioEstado}.
 */
@Service
@Transactional
public class CuestionarioEstadoServiceImpl implements CuestionarioEstadoService {

    private final Logger log = LoggerFactory.getLogger(CuestionarioEstadoServiceImpl.class);

    private final CuestionarioEstadoRepository cuestionarioEstadoRepository;

    public CuestionarioEstadoServiceImpl(CuestionarioEstadoRepository cuestionarioEstadoRepository) {
        this.cuestionarioEstadoRepository = cuestionarioEstadoRepository;
    }

    @Override
    public CuestionarioEstado save(CuestionarioEstado cuestionarioEstado) {
        log.debug("Request to save CuestionarioEstado : {}", cuestionarioEstado);
        return cuestionarioEstadoRepository.save(cuestionarioEstado);
    }

    @Override
    public Optional<CuestionarioEstado> partialUpdate(CuestionarioEstado cuestionarioEstado) {
        log.debug("Request to partially update CuestionarioEstado : {}", cuestionarioEstado);

        return cuestionarioEstadoRepository
            .findById(cuestionarioEstado.getId())
            .map(
                existingCuestionarioEstado -> {
                    if (cuestionarioEstado.getValor() != null) {
                        existingCuestionarioEstado.setValor(cuestionarioEstado.getValor());
                    }
                    if (cuestionarioEstado.getValoracion() != null) {
                        existingCuestionarioEstado.setValoracion(cuestionarioEstado.getValoracion());
                    }

                    return existingCuestionarioEstado;
                }
            )
            .map(cuestionarioEstadoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuestionarioEstado> findAll(Pageable pageable) {
        log.debug("Request to get all CuestionarioEstados");
        return cuestionarioEstadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuestionarioEstado> findOne(Long id) {
        log.debug("Request to get CuestionarioEstado : {}", id);
        return cuestionarioEstadoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CuestionarioEstado : {}", id);
        cuestionarioEstadoRepository.deleteById(id);
    }
}
