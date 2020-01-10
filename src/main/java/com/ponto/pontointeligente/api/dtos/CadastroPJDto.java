package com.ponto.pontointeligente.api.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CadastroPJDto {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String razaoSocial;
    private String cnpj;

    public CadastroPJDto() {

    }

    public Long getId() {
        return id;
    }

    @NotEmpty(message = "Nome não pode ser vazio")
    @Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres")
    public String getNome() {
        return nome;
    }

    @NotEmpty(message = "Email não deve ser vazio")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres")
    @Email(message = "Email no formato inválido")
    public String getEmail() {
        return email;
    }

    @NotEmpty(message = "Senha não pode ser vazia")
    public String getSenha() {
        return senha;
    }

    @NotEmpty(message = "CPF não pode ser vazio")
    @CPF(message = "CPF inválido")
    public String getCpf() {
        return cpf;
    }

    @NotEmpty(message = "Razão social não pode ser vazia")
    @Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres")
    public String getRazaoSocial() {
        return razaoSocial;
    }

    @NotEmpty(message = "Cnpj não pode ser vazio")
    @CNPJ(message = "CNPJ inválido")
    public String getCnpj() {
        return cnpj;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
