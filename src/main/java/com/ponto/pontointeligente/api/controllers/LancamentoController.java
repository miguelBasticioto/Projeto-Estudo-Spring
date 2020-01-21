package com.ponto.pontointeligente.api.controllers;

import com.ponto.pontointeligente.api.dtos.LancamentoDto;
import com.ponto.pontointeligente.api.entities.Funcionario;
import com.ponto.pontointeligente.api.entities.Lancamento;
import com.ponto.pontointeligente.api.enums.TipoEnum;
import com.ponto.pontointeligente.api.response.Response;
import com.ponto.pontointeligente.api.services.FuncionarioService;
import com.ponto.pontointeligente.api.services.LancamentoService;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController() {

    }

    /**
     * Lista lancamentos pelo id de um funcionario
     *
     * @param funcionarioId
     * @param pag
     * @param ord
     * @param dir
     * @return
     */
    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        log.info("Buscando lancamentos pelo funcionario de id {}", funcionarioId);
        Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentoDtos = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));

        response.setData(lancamentoDtos);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna lancamento que possui o id informado
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable(value = "id") Long id) {
        log.info("buscando lancamento de id {}", id);
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (lancamento.isEmpty()) {
            log.info("lancamento de id {} nao encontrado", id);
            response.getErrors().add("Lancamento nao encontrado para id informado");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    /**
     * Adiciona um novo lancamento
     *
     * @param lancamentoDto
     * @param result
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        log.info("Adicionando lancamento");
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        validarFuncionario(lancamentoDto, result);

        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            log.error("Erro validando lancamento");
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um lancamento identificado pelo id
     *
     * @param id
     * @param lancamentoDto
     * @param result
     * @return
     * @throws ParseException
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody LancamentoDto lancamentoDto,
            BindingResult result) throws ParseException {

        log.info("Atualizando lancamento de id {}", id);
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        validarFuncionario(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if(result.hasErrors()) {
            log.error("Erro ao atualizar lancamento de id {}", id);
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Remove lancamento identificado pelo id
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {

        log.info("Removendo lançamento: {}", id);
        Response<String> response = new Response<String>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (lancamento.isEmpty()) {
            log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
            response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        this.lancamentoService.remover(id);
        return ResponseEntity.ok(new Response<String>());

    }

    private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        Lancamento lancamento = new Lancamento();

        if (lancamentoDto.getId().isPresent()) {
            Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
            }
        } else {
            lancamento.setFuncionario(new Funcionario());
            lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
        }

        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.simpleDateFormat.parse(lancamentoDto.getData()));

        if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }

        return lancamento;
    }

    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {

        if (lancamentoDto.getFuncionarioId() == null) {
            result.addError(new ObjectError("funcionario", "funcionario nao informado"));
            return;
        }

        log.info("Validando funcionario de id {}", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
        if (funcionario.isEmpty()) {
            result.addError(new ObjectError("funcionario", "Funcionario nao encontrado. ID inexistente."));
        }

    }

    private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(this.simpleDateFormat.format(lancamento.getData()));
        lancamentoDto.setTipo(lancamento.getTipo().toString());
        lancamentoDto.setDescricao(lancamento.getDescricao());
        lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
        lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());

        return lancamentoDto;
    }
}
