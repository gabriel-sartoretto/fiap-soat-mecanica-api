package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Nested
    @DisplayName("Criação de Email válido")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar Email válido e normalizar para lowercase")
        void deveCriarEmail_quandoValido() {
            // Arrange & Act
            Email email = new Email("TESTE@Email.COM");

            // Assert
            assertThat(email.getValue()).isEqualTo("teste@email.com");
        }

        @Test
        @DisplayName("Deve criar Email com caracteres especiais válidos")
        void deveCriarEmail_quandoComCaracteresEspeciais() {
            // Arrange & Act
            Email email = new Email("user+tag@domain.com");

            // Assert
            assertThat(email.getValue()).isEqualTo("user+tag@domain.com");
        }
    }

    @Nested
    @DisplayName("Criação de Email inválido")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando Email é nulo")
        void deveLancarExcecao_quandoEmailNulo() {
            assertThatThrownBy(() -> new Email(null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Email não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Email é vazio")
        void deveLancarExcecao_quandoEmailVazio() {
            assertThatThrownBy(() -> new Email(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Email não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Email tem formato inválido")
        void deveLancarExcecao_quandoFormatoInvalido() {
            assertThatThrownBy(() -> new Email("emailsemarroba"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Email inválido");
        }
    }
}
