package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CNPJTest {

    @Nested
    @DisplayName("Criação de CNPJ válido")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar CNPJ numérico válido")
        void deveCriarCNPJ_quandoNumericoValido() {
            // Arrange & Act
            CNPJ cnpj = new CNPJ("11222333000181");

            // Assert
            assertThat(cnpj.getValue()).isEqualTo("11222333000181");
        }

        @Test
        @DisplayName("Deve criar CNPJ com máscara")
        void deveCriarCNPJ_quandoComMascara() {
            // Arrange & Act
            CNPJ cnpj = new CNPJ("11.222.333/0001-81");

            // Assert
            assertThat(cnpj.getValue()).isEqualTo("11222333000181");
        }
    }

    @Nested
    @DisplayName("Criação de CNPJ inválido")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ é vazio")
        void deveLancarExcecao_quandoCnpjVazio() {
            assertThatThrownBy(() -> new CNPJ(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CNPJ não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ é inválido")
        void deveLancarExcecao_quandoCnpjInvalido() {
            assertThatThrownBy(() -> new CNPJ("11222333000182"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CNPJ inválido");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ tem todos dígitos iguais")
        void deveLancarExcecao_quandoTodosDigitosIguais() {
            assertThatThrownBy(() -> new CNPJ("11111111111111"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CNPJ inválido");
        }
    }
}
