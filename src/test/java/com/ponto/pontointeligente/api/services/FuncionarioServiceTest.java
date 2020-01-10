package com.ponto.pontointeligente.api.services;

import com.ponto.pontointeligente.api.entities.Funcionario;
import com.ponto.pontointeligente.api.repositories.FuncionarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    @Before
    public void setup() {
        BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
        BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
    }

    @Test
    public void testePersistirFuncionario() {
        Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());

        assertNotNull(funcionario);
    }

    @Test
    public void testeBuscarFuncionarioPorId() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);

        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testeBuscarFuncionarioPorEmail() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("miguelbasticioto1@gmail.com");

        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testeBuscarFuncionarioPorCpf() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("02901780377");

        assertTrue(funcionario.isPresent());
    }
}
