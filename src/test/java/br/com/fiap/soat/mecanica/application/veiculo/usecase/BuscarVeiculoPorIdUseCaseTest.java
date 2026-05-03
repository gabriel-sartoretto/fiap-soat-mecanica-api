package br.com.fiap.soat.mecanica.application.veiculo.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
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
class BuscarVeiculoPorIdUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    @InjectMocks
    private BuscarVeiculoPorIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar veículo por ID com sucesso")
    void deveBuscar_quandoVeiculoExiste() {
        // Arrange
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        when(veiculoRepository.buscarPorId(any())).thenReturn(Optional.of(veiculo));

        // Act
        Veiculo resultado = useCase.executar(veiculo.getId());

        // Assert
        assertThat(resultado).isEqualTo(veiculo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando veículo não encontrado")
    void deveLancarExcecao_quandoNaoEncontrado() {
        when(veiculoRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
