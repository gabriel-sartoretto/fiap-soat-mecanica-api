package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarTodosOrdemServicoPorVeiculoIdUseCaseTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;
    @InjectMocks
    private BuscarTodosOrdemServicoPorVeiculoIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar todas OS por veículo ID")
    void deveBuscar_quandoVeiculoIdValido() {
        // Arrange
        OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
        when(ordemServicoRepository.buscarTodosPorVeiculoId(any())).thenReturn(List.of(os));

        // Act
        List<OrdemServico> resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado).hasSize(1);
    }
}
