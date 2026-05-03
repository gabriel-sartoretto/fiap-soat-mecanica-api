package br.com.fiap.soat.mecanica.application.prestacaoServico;

import br.com.fiap.soat.mecanica.application.alocacaoPeca.InativarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InativarPrestacaoServicoUseCaseTest {

    @Mock
    private PrestacaoServicoRepository prestacaoServicoRepository;
    @Mock
    private AlocacaoPecaRepository alocacaoPecaRepository;
    @Mock
    private InativarAlocacaoPecaUseCase inativarAlocacaoPecaUseCase;
    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @InjectMocks
    private InativarPrestacaoServicoUseCase useCase;

    @Test
    @DisplayName("Deve inativar prestação com alocações ativas")
    void deveInativar_quandoComAlocacoesAtivas() {
        // Arrange
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaValida();

        when(prestacaoServicoRepository.buscarPorId(any()))
                .thenReturn(Optional.of(ps))
                .thenReturn(Optional.of(ps));
        when(ordemServicoRepository.buscarPorId(any())).thenReturn(Optional.of(os));
        when(alocacaoPecaRepository.buscarTodosPorPrestacaoServicoId(any())).thenReturn(List.of(al));
        when(prestacaoServicoRepository.salvar(any())).thenReturn(ps);
        when(ordemServicoRepository.salvar(any())).thenReturn(os);

        // Act
        PrestacaoServico resultado = useCase.executar(ps.getId());

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve inativar prestação sem alocações")
    void deveInativar_quandoSemAlocacoes() {
        // Arrange
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();

        when(prestacaoServicoRepository.buscarPorId(any()))
                .thenReturn(Optional.of(ps))
                .thenReturn(Optional.of(ps));
        when(ordemServicoRepository.buscarPorId(any())).thenReturn(Optional.of(os));
        when(alocacaoPecaRepository.buscarTodosPorPrestacaoServicoId(any())).thenReturn(List.of());
        when(prestacaoServicoRepository.salvar(any())).thenReturn(ps);
        when(ordemServicoRepository.salvar(any())).thenReturn(os);

        // Act
        PrestacaoServico resultado = useCase.executar(ps.getId());

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando prestação não encontrada")
    void deveLancarExcecao_quandoNaoEncontrada() {
        when(prestacaoServicoRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
