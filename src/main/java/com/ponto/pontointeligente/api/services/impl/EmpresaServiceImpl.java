package com.ponto.pontointeligente.api.services.impl;

import com.ponto.pontointeligente.api.entities.Empresa;
import com.ponto.pontointeligente.api.repositories.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaServiceImpl implements com.ponto.pontointeligente.api.services.EmpresaService {
    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    EmpresaRepository empresaRepository;

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        log.info("buscando empresa cnpj {}", cnpj);
        return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        log.info("persistindo empresa {}", empresa);
        return this.empresaRepository.save(empresa);
    }
}
