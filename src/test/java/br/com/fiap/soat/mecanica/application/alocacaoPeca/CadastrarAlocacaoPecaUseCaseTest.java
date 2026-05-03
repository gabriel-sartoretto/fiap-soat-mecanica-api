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
class CadastrarAlocacaoPecaUseCaseTest {

    @Mock
    private AlocacaoPecaRepository repository;
    @Mock
    private PecaRepository pecaRepository;
    @Mock
    private PrestacaoServicoRepository prestacaoServicoRepository;
    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @InjectMocks
    private CadastrarAlocacaoPecaUseCase useCase;

    @Test
    @DisplayName("Deve cadastrar alocação de peça com sucesso")
    void deveCadastrar_quandoDadosValidos() {
        // Arrange
        Peca peca = TestDataFactory.criarPecaValida();
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaValida();

        when(repository.existsByPrestacaoServicoIdAndPecaId(any(), any())).thenReturn(false);
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.of(peca));
        when(prestacaoServicoRepository.buscarPorId(any())).thenReturn(Optional.of(ps));
        when(ordemServicoRepository.buscarPorId(any())).thenReturn(Optional.of(os));
        when(repository.salvar(any())).thenReturn(al);
        when(pecaRepository.salvar(any())).thenReturn(peca);
        when(prestacaoServicoRepository.salvar(any())).thenReturn(ps);
        when(ordemServicoRepository.salvar(any())).thenReturn(os);

        // Act
        AlocacaoPeca resultado = useCase.executar(2, UUID.randomUUID(), UUID.randomUUID());

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando peça já alocada")
    void deveLancarExcecao_quandoPecaJaAlocada() {
        when(repository.existsByPrestacaoServicoIdAndPecaId(any(), any())).thenReturn(true);
        assertThatThrownBy(() -> useCase.executar(2, UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando peça não encontrada")
    void deveLancarExcecao_quandoPecaNaoEncontrada() {
        when(repository.existsByPrestacaoServicoIdAndPecaId(any(), any())).thenReturn(false);
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(2, UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando prestação não encontrada")
    void deveLancarExcecao_quandoPrestacaoNaoEncontrada() {
        Peca peca = TestDataFactory.criarPecaValida();
        when(repository.existsByPrestacaoServicoIdAndPecaId(any(), any())).thenReturn(false);
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.of(peca));
        when(prestacaoServicoRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(2, UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
