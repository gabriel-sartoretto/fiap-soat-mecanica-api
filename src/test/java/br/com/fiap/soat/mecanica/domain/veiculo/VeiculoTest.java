package br.com.fiap.soat.mecanica.domain.veiculo;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VeiculoTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar Veiculo com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, UUID.randomUUID());

            // Assert
            assertThat(veiculo.getMarca()).isEqualTo("Toyota");
            assertThat(veiculo.getModelo()).isEqualTo("Corolla");
        }

        @Test
        @DisplayName("Deve lançar exceção quando placa é nula")
        void deveLancarExcecao_quandoPlacaNula() {
            assertThatThrownBy(() -> new Veiculo(null, "Toyota", "Corolla", "2023", 2, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando marca é nula")
        void deveLancarExcecao_quandoMarcaNula() {
            assertThatThrownBy(() -> new Veiculo(new Placa("ABC1234"), null, "Corolla", "2023", 2, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando modelo é nulo")
        void deveLancarExcecao_quandoModeloNulo() {
            assertThatThrownBy(() -> new Veiculo(new Placa("ABC1234"), "Toyota", null, "2023", 2, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando ano é nulo")
        void deveLancarExcecao_quandoAnoNulo() {
            assertThatThrownBy(() -> new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", null, 2, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando quantidadeEixos é nula")
        void deveLancarExcecao_quandoEixosNulo() {
            assertThatThrownBy(() -> new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando clienteId é nulo")
        void deveLancarExcecao_quandoClienteIdNulo() {
            assertThatThrownBy(() -> new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, null))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Alterar")
    class Alterar {

        @Test
        @DisplayName("Deve alterar dados do veículo")
        void deveAlterar_quandoDadosValidos() {
            // Arrange
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, UUID.randomUUID());

            // Act
            veiculo.alterar("Honda", "Civic", "2024", 2);

            // Assert
            assertThat(veiculo.getMarca()).isEqualTo("Honda");
            assertThat(veiculo.getModelo()).isEqualTo("Civic");
            assertThat(veiculo.getAno()).isEqualTo("2024");
        }
    }
}
