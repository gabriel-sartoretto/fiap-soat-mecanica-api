package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelefoneTest {

    @Nested
    @DisplayName("Criação de Telefone válido")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar Telefone com 10 dígitos")
        void deveCriarTelefone_quando10Digitos() {
            // Arrange & Act
            Telefone telefone = new Telefone("1133445566");

            // Assert
            assertThat(telefone.getValue()).isEqualTo("1133445566");
        }

        @Test
        @DisplayName("Deve criar Telefone com 11 dígitos")
        void deveCriarTelefone_quando11Digitos() {
            // Arrange & Act
            Telefone telefone = new Telefone("11999887766");

            // Assert
            assertThat(telefone.getValue()).isEqualTo("11999887766");
        }
    }

    @Nested
    @DisplayName("Criação de Telefone inválido")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando Telefone é nulo")
        void deveLancarExcecao_quandoTelefoneNulo() {
            assertThatThrownBy(() -> new Telefone(null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Telefone não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Telefone é vazio")
        void deveLancarExcecao_quandoTelefoneVazio() {
            assertThatThrownBy(() -> new Telefone(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Telefone não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Telefone tem menos de 10 dígitos")
        void deveLancarExcecao_quandoMenosDe10Digitos() {
            assertThatThrownBy(() -> new Telefone("123456789"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Telefone deve ter 10 ou 11 dígitos");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Telefone tem mais de 11 dígitos")
        void deveLancarExcecao_quandoMaisDe11Digitos() {
            assertThatThrownBy(() -> new Telefone("123456789012"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Telefone deve ter 10 ou 11 dígitos");
        }
    }
}
