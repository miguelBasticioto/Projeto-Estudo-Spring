package com.ponto.pontointeligente.api.repositories;

import com.ponto.pontointeligente.api.entities.Empresa;
import com.ponto.pontointeligente.api.entities.Funcionario;
import com.ponto.pontointeligente.api.entities.Lancamento;
import com.ponto.pontointeligente.api.enums.PerfilEnum;
import com.ponto.pontointeligente.api.enums.TipoEnum;
import com.ponto.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    private Long funcionarioId;

    @Before
    public void setup(){
        Empresa empresa = this.empresaRepository.save(getEmpresa());

        Funcionario funcionario = this.funcionarioRepository.save(getFuncionario(empresa));
        this.funcionarioId = funcionario.getId();

        this.lancamentoRepository.save(getLancamento(funcionario));
        this.lancamentoRepository.save(getLancamento(funcionario));
    }

    @After
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentoPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);

        assertEquals(2, lancamentos.size());
    }

    @Test
    public void testBuscarLancamentoPorFuncionarioIdPaginado() {
        PageRequest page = PageRequest.of(0, 10);
        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);

        assertEquals(2, lancamentos.getTotalElements());
    }

    private Empresa getEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Razao social de teste");
        empresa.setCnpj("123123123123");
        return empresa;
    }

    private Funcionario getFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Teste");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
        funcionario.setCpf("02901780377");
        funcionario.setEmail("miguelbasticioto1@gmail.com");
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

    private Lancamento getLancamento(Funcionario funcionario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }
}
