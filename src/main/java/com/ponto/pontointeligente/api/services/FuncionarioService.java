package com.ponto.pontointeligente.api.services;

import com.ponto.pontointeligente.api.entities.Funcionario;

import java.util.Optional;

public interface FuncionarioService {
    Funcionario persistir(Funcionario funcionario);

    Optional<Funcionario> buscarPorCpf(String cpf);

    Optional<Funcionario> buscarPorEmail(String cpf);

    Optional<Funcionario> buscarPorId(Long id);
}
