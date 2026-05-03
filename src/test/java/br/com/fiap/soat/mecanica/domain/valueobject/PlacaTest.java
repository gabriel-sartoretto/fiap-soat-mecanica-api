package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlacaTest {

    @Nested
    @DisplayName("Criação de Placa válida")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar Placa no formato antigo (AAA9999)")
        void deveCriarPlaca_quandoFormatoAntigo() {
            // Arrange & Act
            Placa placa = new Placa("ABC1234");

            // Assert
            assertThat(placa.getValue()).isEqualTo("ABC1234");
        }

        @Test
        @DisplayName("Deve criar Placa no formato Mercosul (AAA9A99)")
        void deveCriarPlaca_quandoFormatoMercosul() {
            // Arrange & Act
            Placa placa = new Placa("ABC1D23");

            // Assert
            assertThat(placa.getValue()).isEqualTo("ABC1D23");
        }

        @Test
        @DisplayName("Deve normalizar Placa para uppercase")
        void deveCriarPlaca_quandoLowercase() {
            // Arrange & Act
            Placa placa = new Placa("abc1234");

            // Assert
            assertThat(placa.getValue()).isEqualTo("ABC1234");
        }
    }

    @Nested
    @DisplayName("Criação de Placa inválida")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando Placa é nula")
        void deveLancarExcecao_quandoPlacaNula() {
            assertThatThrownBy(() -> new Placa(null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Placa é obrigatória");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Placa é vazia")
        void deveLancarExcecao_quandoPlacaVazia() {
            assertThatThrownBy(() -> new Placa(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Placa é obrigatória");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Placa tem formato inválido")
        void deveLancarExcecao_quandoPlacaInvalida() {
            assertThatThrownBy(() -> new Placa("12345"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Placa inválida");
        }
    }
}
