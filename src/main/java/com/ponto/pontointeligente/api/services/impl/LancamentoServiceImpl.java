package com.ponto.pontointeligente.api.services.impl;

import com.ponto.pontointeligente.api.entities.Lancamento;
import com.ponto.pontointeligente.api.repositories.LancamentoRepository;
import com.ponto.pontointeligente.api.services.LancamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {
    private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Override
    public Page<Lancamento> buscarPorFuncionarioId(Long id, PageRequest pageRequest) {
        log.info("Buscando por funcionario id {}", id);
        return this.lancamentoRepository.findByFuncionarioId(id, pageRequest);
    }

    @Override
    public Optional<Lancamento> buscarPorId(Long id) {
        log.info("Buscando lancamento id {}", id);
        return this.lancamentoRepository.findById(id);
    }

    @Override
    public Lancamento persistir(Lancamento lancamento) {
        log.info("Persistindo lancamentos");
        return this.lancamentoRepository.save(lancamento);
    }

    @Override
    public void remover(Long id) {

    }
}
