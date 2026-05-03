package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SenhaTest {

    @Nested
    @DisplayName("Criação de Senha válida")
    class CriacaoValida {

        @Test
        @DisplayName("Deve criar Senha válida")
        void deveCriarSenha_quandoValida() {
            // Arrange & Act
            Senha senha = new Senha("Senha@123");

            // Assert
            assertThat(senha.getValor()).isEqualTo("Senha@123");
        }
    }

    @Nested
    @DisplayName("Criação de Senha inválida")
    class CriacaoInvalida {

        @Test
        @DisplayName("Deve lançar exceção quando Senha é nula")
        void deveLancarExcecao_quandoSenhaNula() {
            assertThatThrownBy(() -> new Senha(null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha é obrigatória");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Senha é vazia")
        void deveLancarExcecao_quandoSenhaVazia() {
            assertThatThrownBy(() -> new Senha(""))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha é obrigatória");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Senha tem menos de 8 caracteres")
        void deveLancarExcecao_quandoSenhaCurta() {
            assertThatThrownBy(() -> new Senha("Se@1"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha deve ter no mínimo 8 caracteres");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Senha tem mais de 100 caracteres")
        void deveLancarExcecao_quandoSenhaLonga() {
            String senhaLonga = "A@1" + "a".repeat(98);
            assertThatThrownBy(() -> new Senha(senhaLonga))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha muito longa");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Senha não contém número")
        void deveLancarExcecao_quandoSemNumero() {
            assertThatThrownBy(() -> new Senha("SenhaSem@Numero"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha deve conter ao menos um número");
        }

        @Test
        @DisplayName("Deve lançar exceção quando Senha não contém caractere especial")
        void deveLancarExcecao_quandoSemEspecial() {
            assertThatThrownBy(() -> new Senha("SenhaSem1Especial"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Senha deve conter ao menos um caractere especial");
        }
    }
}
