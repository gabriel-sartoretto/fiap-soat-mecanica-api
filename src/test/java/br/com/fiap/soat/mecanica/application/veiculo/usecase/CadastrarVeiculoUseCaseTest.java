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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoUseCaseTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    @InjectMocks
    private CadastrarVeiculoUseCase useCase;

    @Test
    @DisplayName("Deve cadastrar veículo com sucesso")
    void deveCadastrar_quandoDadosValidos() {
        // Arrange
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        when(veiculoRepository.buscarPorPlaca(anyString())).thenReturn(Optional.empty());
        when(veiculoRepository.salvar(any())).thenReturn(veiculo);

        // Act
        Veiculo resultado = useCase.executar("ABC1234", "Toyota", "Corolla", "2023", 2, UUID.randomUUID());

        // Assert
        assertThat(resultado).isNotNull();
    }
}
