package com.ponto.pontointeligente.api.controllers;

import com.ponto.pontointeligente.api.dtos.EmpresaDto;
import com.ponto.pontointeligente.api.entities.Empresa;
import com.ponto.pontointeligente.Test;
import com.ponto.pontointeligente.api.response.Response;
import com.ponto.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    private EmpresaService empresaService;

    public EmpresaController() {

    }

    @GetMapping(value = "/request/test")
    public ResponseEntity<Response<Test>> externalRequestGetTest() {

        final RestTemplate restTemplate = new RestTemplate();
        final Test result = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", Test.class);

        Response<Test> response = new Response<>();
        response.setData(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/request/test")
    public ResponseEntity<Response<Test>> externalRequestPostTest(@RequestBody Test test) {

        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        HttpEntity<Test> entity = new HttpEntity<>(test, headers);

        final Test result = restTemplate.postForObject("https://jsonplaceholder.typicode.com/posts", entity, Test.class);

        Response<Test> response = new Response<>();
        response.setData(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscardPorCnpj(@PathVariable("cnpj") String cnpj) {
        log.info("Buscando por cnpj {}", cnpj);

        Response<EmpresaDto> response = new Response<EmpresaDto>();
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

        if (empresa.isEmpty()) {
            log.info("Empresa nao encontrada");
            response.getErrors().add("Empresa não encontrada para esse cnpj");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertEmpresaDto(empresa.get()));
        return ResponseEntity.ok(response);
    }

    private EmpresaDto convertEmpresaDto(Empresa empresa) {
        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setCnpj(empresa.getCnpj());
        empresaDto.setRazaoSocial(empresa.getRazaoSocial());

        return empresaDto;
    }

}
