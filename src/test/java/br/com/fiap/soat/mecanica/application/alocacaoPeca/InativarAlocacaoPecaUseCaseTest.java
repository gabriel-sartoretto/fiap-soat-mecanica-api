package br.com.fiap.soat.mecanica.application.alocacaoPeca;

import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InativarAlocacaoPecaUseCaseTest {

    @Mock
    private AlocacaoPecaRepository alocacaoPecaRepository;
    @Mock
    private PecaRepository pecaRepository;
    @Mock
    private PrestacaoServicoRepository prestacaoServicoRepository;
    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @InjectMocks
    private InativarAlocacaoPecaUseCase useCase;

    @Test
    @DisplayName("Deve inativar alocação de peça com sucesso")
    void deveInativar_quandoAlocacaoAtiva() {
        // Arrange
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaValida();
        Peca peca = TestDataFactory.criarPecaValida();
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();

        when(alocacaoPecaRepository.buscarPorId(any())).thenReturn(Optional.of(al));
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.of(peca));
        when(prestacaoServicoRepository.buscarPorId(any())).thenReturn(Optional.of(ps));
        when(ordemServicoRepository.buscarPorId(any())).thenReturn(Optional.of(os));
        when(alocacaoPecaRepository.salvar(any())).thenReturn(al);

        // Act
        AlocacaoPeca resultado = useCase.executar(al.getId());

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando alocação já inativa")
    void deveLancarExcecao_quandoJaInativa() {
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaInativa();
        when(alocacaoPecaRepository.buscarPorId(any())).thenReturn(Optional.of(al));
        assertThatThrownBy(() -> useCase.executar(al.getId()))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando alocação não encontrada")
    void deveLancarExcecao_quandoNaoEncontrada() {
        when(alocacaoPecaRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
