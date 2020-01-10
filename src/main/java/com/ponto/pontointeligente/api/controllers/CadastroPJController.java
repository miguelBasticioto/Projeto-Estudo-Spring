package com.ponto.pontointeligente.api.controllers;

import com.ponto.pontointeligente.api.dtos.CadastroPJDto;
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

@RestController
@RequestMapping("/api/cadastrar-pessoa")
public class CadastroPJController {

    private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    public CadastroPJController() {

    }

    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto pjDto, BindingResult result) {
        log.info("Cadastrando pessoa");

        Response<CadastroPJDto> response = new Response<CadastroPJDto>();

        validarDadosExistentes(pjDto, result);
        Empresa empresa = this.convertDtoParaEmpresa(pjDto);
        Funcionario funcionario = this.convertDtoParaFuncionario(pjDto, result);

        if(result.hasErrors()) {
            log.info("Erro ao registrar pessoa juridica");
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPJDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private void validarDadosExistentes(CadastroPJDto pjDto, BindingResult result) {
        this.empresaService.buscarPorCnpj(pjDto.getCnpj())
                .ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente")));

        this.funcionarioService.buscarPorCpf(pjDto.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF ja existente")));

        this.funcionarioService.buscarPorEmail(pjDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email ja existente")));

    }

    private Empresa convertDtoParaEmpresa(CadastroPJDto pjDto) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(pjDto.getCnpj());
        empresa.setRazaoSocial(pjDto.getRazaoSocial());

        return empresa;
    }

    private Funcionario convertDtoParaFuncionario(CadastroPJDto pjDto, BindingResult result) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(pjDto.getNome());
        funcionario.setEmail(pjDto.getEmail());
        funcionario.setCpf(pjDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(pjDto.getSenha()));

        return funcionario;
    }

    private CadastroPJDto converterCadastroPJDto (Funcionario funcionario) {
        CadastroPJDto cadastroPJDto = new CadastroPJDto();
        cadastroPJDto.setId(funcionario.getId());
        cadastroPJDto.setNome(funcionario.getNome());
        cadastroPJDto.setEmail(funcionario.getEmail());
        cadastroPJDto.setCpf(funcionario.getCpf());
        cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
        cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

        return cadastroPJDto;
    }
}
