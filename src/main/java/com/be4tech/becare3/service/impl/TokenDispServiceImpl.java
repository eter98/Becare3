package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.TokenDisp;
import com.be4tech.becare3.repository.TokenDispRepository;
import com.be4tech.becare3.service.TokenDispService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TokenDisp}.
 */
@Service
@Transactional
public class TokenDispServiceImpl implements TokenDispService {

    private final Logger log = LoggerFactory.getLogger(TokenDispServiceImpl.class);

    private final TokenDispRepository tokenDispRepository;

    public TokenDispServiceImpl(TokenDispRepository tokenDispRepository) {
        this.tokenDispRepository = tokenDispRepository;
    }

    @Override
    public TokenDisp save(TokenDisp tokenDisp) {
        log.debug("Request to save TokenDisp : {}", tokenDisp);
        return tokenDispRepository.save(tokenDisp);
    }

    @Override
    public Optional<TokenDisp> partialUpdate(TokenDisp tokenDisp) {
        log.debug("Request to partially update TokenDisp : {}", tokenDisp);

        return tokenDispRepository
            .findById(tokenDisp.getId())
            .map(
                existingTokenDisp -> {
                    if (tokenDisp.getTokenConexion() != null) {
                        existingTokenDisp.setTokenConexion(tokenDisp.getTokenConexion());
                    }
                    if (tokenDisp.getActivo() != null) {
                        existingTokenDisp.setActivo(tokenDisp.getActivo());
                    }
                    if (tokenDisp.getFechaInicio() != null) {
                        existingTokenDisp.setFechaInicio(tokenDisp.getFechaInicio());
                    }
                    if (tokenDisp.getFechaFin() != null) {
                        existingTokenDisp.setFechaFin(tokenDisp.getFechaFin());
                    }

                    return existingTokenDisp;
                }
            )
            .map(tokenDispRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TokenDisp> findAll(Pageable pageable) {
        log.debug("Request to get all TokenDisps");
        return tokenDispRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TokenDisp> findOne(Long id) {
        log.debug("Request to get TokenDisp : {}", id);
        return tokenDispRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TokenDisp : {}", id);
        tokenDispRepository.deleteById(id);
    }
}
