package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CPFTest {

    @Nested
    @DisplayName("Criação de CPF válido")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar CPF com 11 dígitos válidos")
        void deveCriarCPF_quandoDigitosValidos() {
            // Arrange & Act
            CPF cpf = new CPF("52998224725");

            // Assert
            assertThat(cpf.getValue()).isEqualTo("52998224725");
        }

        @Test
        @DisplayName("Deve criar CPF removendo máscara")
        void deveCriarCPF_quandoComMascara() {
            // Arrange & Act
            CPF cpf = new CPF("529.982.247-25");

            // Assert
            assertThat(cpf.getValue()).isEqualTo("52998224725");
        }
    }

    @Nested
    @DisplayName("Criação de CPF inválido")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando CPF é nulo")
        void deveLancarExcecao_quandoCpfNulo() {
            assertThatThrownBy(() -> new CPF(null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CPF não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF é vazio")
        void deveLancarExcecao_quandoCpfVazio() {
            assertThatThrownBy(() -> new CPF(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CPF não pode ser vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF tem tamanho errado")
        void deveLancarExcecao_quandoCpfTamanhoErrado() {
            assertThatThrownBy(() -> new CPF("1234567890"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CPF inválido");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF tem dígitos verificadores inválidos")
        void deveLancarExcecao_quandoDigitosInvalidos() {
            assertThatThrownBy(() -> new CPF("52998224726"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CPF inválido");
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF tem todos dígitos iguais")
        void deveLancarExcecao_quandoTodosDigitosIguais() {
            assertThatThrownBy(() -> new CPF("11111111111"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("CPF inválido");
        }
    }
}
