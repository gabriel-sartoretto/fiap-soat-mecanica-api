package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarTodosOrdemServicoPorVeiculoPlacaUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @InjectMocks
    private BuscarTodosOrdemServicoPorVeiculoPlacaUseCase useCase;

    @Test
    @DisplayName("Deve buscar ordens de servico por placa do veiculo")
    void deveBuscarOrdensPorPlaca() {
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        OrdemServico ordemServico = TestDataFactory.criarOrdemServicoRecebida();
        when(veiculoRepository.buscarPorPlaca(any())).thenReturn(Optional.of(veiculo));
        when(ordemServicoRepository.buscarTodosPorVeiculoId(veiculo.getId())).thenReturn(List.of(ordemServico));

        List<OrdemServico> resultado = useCase.executar("ABC1234");

        assertThat(resultado).containsExactly(ordemServico);
    }

    @Test
    @DisplayName("Deve lancar excecao quando veiculo nao existe")
    void deveLancarExcecao_quandoVeiculoNaoExiste() {
        when(veiculoRepository.buscarPorPlaca(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar("ABC1234"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
