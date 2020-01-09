package com.ponto.pontointeligente.api.entities;

import com.ponto.pontointeligente.api.enums.TipoEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {

    private static final long serialVersionUID = 6524560251526772839L;

    private Long id;
    private Date data;
    private String descricao;
    private String localizacao;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private TipoEnum tipo;
    private Funcionario funcionario;

    public Lancamento () {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    public Date getData() {
        return data;
    }

    @Column(name = "descricao", nullable = true)
    public String getDescricao() {
        return descricao;
    }

    @Column(name = "localizacao", nullable = true)
    public String getLocalizacao() {
        return localizacao;
    }

    @Column(name = "data_criacao", nullable = false)
    public Date getDataCriacao() {
        return dataCriacao;
    }

    @Column(name = "data_atualizacao", nullable = false)
    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    public TipoEnum getTipo() {
        return tipo;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Funcionario getFuncionario() {
        return funcionario;
    }

    @PreUpdate
    public void preUpdate () {
        dataAtualizacao = new Date();
    }

    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAtualizacao(Date dataAtulizacao) {
        this.dataAtualizacao = dataAtulizacao;
    }

    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
