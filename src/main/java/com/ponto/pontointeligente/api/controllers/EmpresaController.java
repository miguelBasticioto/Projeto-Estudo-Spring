package com.ponto.pontointeligente.api.controllers;

import com.google.gson.Gson;
import com.ponto.pontointeligente.api.dtos.EmpresaDto;
import com.ponto.pontointeligente.api.entities.Empresa;
import com.ponto.pontointeligente.api.models.Test;
import com.ponto.pontointeligente.api.response.Response;
import com.ponto.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Response<Test>> externalRequestTest() {

        final RestTemplate restTemplate = new RestTemplate();
        final String result = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", String.class);

        Gson gson = new Gson();
        Test test = gson.fromJson(result, Test.class);

        Response<Test> response = new Response<>();
        response.setData(test);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscardPorCnpj(@PathVariable("cnpj") String cnpj) {
        log.info("Buscando por cnpj {}", cnpj);

        Response<EmpresaDto> response = new Response<EmpresaDto>();
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

        if(empresa.isEmpty()) {
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
