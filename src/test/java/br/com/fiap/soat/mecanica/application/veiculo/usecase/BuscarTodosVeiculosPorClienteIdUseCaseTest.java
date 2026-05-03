package br.com.fiap.soat.mecanica.application.veiculo.usecase;

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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarTodosVeiculosPorClienteIdUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    @InjectMocks
    private BuscarTodosVeiculosPorClienteIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar todos veículos por cliente ID")
    void deveBuscar_quandoClienteIdValido() {
        // Arrange
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        when(veiculoRepository.buscarTodosPorClienteId(any())).thenReturn(List.of(veiculo));

        // Act
        List<Veiculo> resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado).hasSize(1);
    }
}
