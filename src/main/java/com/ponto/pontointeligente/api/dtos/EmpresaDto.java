package com.ponto.pontointeligente.api.dtos;

public class EmpresaDto {
    private Long id;
    private String razaoSocial;
    private String cnpj;

    public EmpresaDto() {

    }

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
