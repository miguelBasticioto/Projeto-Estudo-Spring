package com.ponto.pontointeligente.api.controllers;

import com.ponto.pontointeligente.api.dtos.CadastroPFDto;
import com.ponto.pontointeligente.api.entities.Empresa;
import com.ponto.pontointeligente.api.entities.Funcionario;
import com.ponto.pontointeligente.api.enums.PerfilEnum;
import com.ponto.pontointeligente.api.response.Response;
import com.ponto.pontointeligente.api.services.EmpresaService;
import com.ponto.pontointeligente.api.services.FuncionarioService;
import com.ponto.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/cadastrar-pessoa-fisica")
public class CadastroPFController {
    private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FuncionarioService funcionarioService;

    public CadastroPFController() {

    }

    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto pfDto, BindingResult result) {
        log.info("Cadastrando pessoa fisica");

        Response<CadastroPFDto> response = new Response<CadastroPFDto>();

        validarDadosExistentes(pfDto, result);

        Funcionario funcionario = this.convertDtoParaFuncionario(pfDto, result);

        if (result.hasErrors()) {
            log.info("Erro ao cadastrar pessoa física");
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(pfDto.getCnpj());
        empresa.ifPresent(funcionario::setEmpresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(this.convertCadastroPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private void validarDadosExistentes(CadastroPFDto pfDto, BindingResult result) {
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(pfDto.getCnpj());
        if (!empresa.isPresent()) {
            result.addError(new ObjectError("empresa", "Empresa não cadastrada"));
        }

        this.funcionarioService.buscarPorCpf(pfDto.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Cpf ja cadastrado")));

        this.funcionarioService.buscarPorEmail(pfDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email ja cadastrado")));
    }

    private Funcionario convertDtoParaFuncionario(CadastroPFDto pfDto, BindingResult result) {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(pfDto.getNome());
        funcionario.setEmail(pfDto.getEmail());
        funcionario.setCpf(pfDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(pfDto.getSenha()));

        pfDto.getQtdHorasAlmoco().ifPresent(horas -> {
            funcionario.setQtdHorasAlmoco(Float.valueOf(horas));
        });

        pfDto.getQtdHorasTrabalhadasDia().ifPresent(horas -> {
            funcionario.setQtdHorasTrabalhoDia(Float.valueOf(horas));
        });

        pfDto.getValorHora().ifPresent(valor -> {
            funcionario.setValorHora(new BigDecimal(valor));
        });

        return funcionario;
    }

    private CadastroPFDto convertCadastroPFDto(Funcionario funcionario) {
        CadastroPFDto cadastroPFDto = new CadastroPFDto();

        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setNome(funcionario.getNome());

        funcionario.getQtdHorasAlmocoOpt()
                .ifPresent(horas -> {
                    cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(horas)));
                });

        funcionario.getQtdHorasTrabalhoDiaOpt()
                .ifPresent(horas -> {
                    cadastroPFDto.setQtdHorasTrabalhadasDia(Optional.of(Float.toString(horas)));
                });

        funcionario.getValorHoraOpt()
                .ifPresent(valor -> {
                    cadastroPFDto.setValorHora(Optional.of(valor.toString()));
                });

        return cadastroPFDto;
    }
}
