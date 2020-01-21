package com.ponto.pontointeligente.api.controllers;

import com.ponto.pontointeligente.api.dtos.FuncionarioDto;
import com.ponto.pontointeligente.api.entities.Funcionario;
import com.ponto.pontointeligente.api.response.Response;
import com.ponto.pontointeligente.api.services.FuncionarioService;
import com.ponto.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

    @Autowired
    private FuncionarioService funcionarioService;

    public FuncionarioController() {

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) {
        log.info("Atualizando funcionario de id {}", id);
        Response<FuncionarioDto> response = new Response<>();

        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
        if(funcionario.isEmpty()) {
            result.addError(new ObjectError("funcionario", "Funcionario não encontrado."));
        }

        this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);

        if(result.hasErrors()) {
            log.error("Erro ao atualizar o funcionario {}", id);
            result.getAllErrors().forEach(error -> {
                response.getErrors().add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }
        this.funcionarioService.persistir(funcionario.get());
        response.setData(this.converterFuncionarioDto(funcionario.get()));

        return ResponseEntity.ok(response);
    }

    private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setId(funcionario.getId());
        funcionarioDto.setEmail(funcionarioDto.getEmail());
        funcionarioDto.setNome(funcionarioDto.getNome());

        funcionario.getQtdHorasAlmocoOpt().ifPresent(horas -> {
            funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(horas)));
        });

        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(horas -> {
            funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(horas)));
        });

        funcionario.getValorHoraOpt().ifPresent(valor -> {
            funcionarioDto.setValorHora(Optional.of(valor.toString()));
        });

        return funcionarioDto;
    }

    private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) {

        funcionario.setNome(funcionarioDto.getNome());

        if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
            this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
                    .ifPresent(func -> {
                        result.addError(new ObjectError("email", "Email já existente."));
                    });
            funcionario.setEmail(funcionarioDto.getEmail());
        }

        funcionario.setQtdHorasAlmoco(null);
        funcionarioDto.getQtdHorasAlmoco().ifPresent(horas -> {
            funcionario.setQtdHorasAlmoco(Float.valueOf(horas));
        });

        funcionario.setQtdHorasTrabalhoDia(null);
        funcionarioDto.getQtdHorasTrabalhoDia().ifPresent(horas -> {
            funcionario.setQtdHorasTrabalhoDia(Float.valueOf(horas));
        });

        funcionario.setValorHora(null);
        funcionarioDto.getValorHora().ifPresent(valor -> {
            funcionario.setValorHora(new BigDecimal(valor));
        });

        if(funcionarioDto.getSenha().isPresent()) {
            funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
        }
    }
}
