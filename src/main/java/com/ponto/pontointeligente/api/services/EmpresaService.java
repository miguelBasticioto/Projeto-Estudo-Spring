package com.ponto.pontointeligente.api.services;

import com.ponto.pontointeligente.api.entities.Empresa;

import java.util.Optional;

public interface EmpresaService {

    Optional<Empresa> buscarPorCnpj(String cnpj);

    Empresa persistir(Empresa empresa);
}
