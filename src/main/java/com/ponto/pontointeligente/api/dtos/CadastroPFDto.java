package com.ponto.pontointeligente.api.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class CadastroPFDto {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private Optional<String> valorHora = Optional.empty();
    private Optional<String> qtdHorasTrabalhadasDia = Optional.empty();
    private Optional<String> qtdHorasAlmoco = Optional.empty();
    private String cnpj;

    public CadastroPFDto () {

    }

    public Long getId() {
        return id;
    }

    @NotEmpty(message = "Nome não pode ser vazio")
    @Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres")
    public String getNome() {
        return nome;
    }

    @NotEmpty(message = "Email não pode ser vazio")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres")
    @Email(message = "Email inválido")
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

    @NotEmpty(message = "CNPJ não pode ser vazio")
    @CNPJ(message = "CNPJ inválido")
    public String getCnpj() {
        return cnpj;
    }

    public Optional<String> getValorHora() {
        return valorHora;
    }

    public Optional<String> getQtdHorasTrabalhadasDia() {
        return qtdHorasTrabalhadasDia;
    }

    public Optional<String> getQtdHorasAlmoco() {
        return qtdHorasAlmoco;
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

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setValorHora(Optional<String> valorHora) {
        this.valorHora = valorHora;
    }

    public void setQtdHorasTrabalhadasDia(Optional<String> qtdHorasTrabalhadasDia) {
        this.qtdHorasTrabalhadasDia = qtdHorasTrabalhadasDia;
    }

    public void setQtdHorasAlmoco(Optional<String> qtdHorasAlmoco) {
        this.qtdHorasAlmoco = qtdHorasAlmoco;
    }
}
