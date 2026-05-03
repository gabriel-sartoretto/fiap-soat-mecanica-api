package br.com.fiap.soat.mecanica.domain.peca;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PecaTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar Peça com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

            // Assert
            assertThat(peca.getNome()).isEqualTo("Pastilha");
            assertThat(peca.getMarca()).isEqualTo("Bosch");
            assertThat(peca.getValorUnitario()).isEqualByComparingTo(new BigDecimal("150.00"));
            assertThat(peca.getQuantidadeEstoque()).isEqualTo(10);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é nulo")
        void deveLancarExcecao_quandoNomeNulo() {
            assertThatThrownBy(() -> new Peca(null, "Bosch", new BigDecimal("150"), 10))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é vazio")
        void deveLancarExcecao_quandoNomeVazio() {
            assertThatThrownBy(() -> new Peca("", "Bosch", new BigDecimal("150"), 10))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando marca é nula")
        void deveLancarExcecao_quandoMarcaNula() {
            assertThatThrownBy(() -> new Peca("Pastilha", null, new BigDecimal("150"), 10))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor unitário é negativo")
        void deveLancarExcecao_quandoValorNegativo() {
            assertThatThrownBy(() -> new Peca("Pastilha", "Bosch", new BigDecimal("-1"), 10))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando quantidade estoque é negativa")
        void deveLancarExcecao_quandoEstoqueNegativo() {
            assertThatThrownBy(() -> new Peca("Pastilha", "Bosch", new BigDecimal("150"), -1))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Alterar")
    class Alterar {

        @Test
        @DisplayName("Deve alterar dados da peça")
        void deveAlterar_quandoDadosValidos() {
            // Arrange
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

            // Act
            peca.alterar("Disco", "Fremax", new BigDecimal("200.00"), 5);

            // Assert
            assertThat(peca.getNome()).isEqualTo("Disco");
            assertThat(peca.getMarca()).isEqualTo("Fremax");
        }
    }

    @Nested
    @DisplayName("Estoque")
    class Estoque {

        @Test
        @DisplayName("Deve baixar estoque com sucesso")
        void deveBaixarEstoque_quandoQuantidadeValida() {
            // Arrange
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

            // Act
            peca.baixarEstoque(3);

            // Assert
            assertThat(peca.getQuantidadeEstoque()).isEqualTo(7);
        }

        @Test
        @DisplayName("Deve lançar exceção ao baixar estoque com quantidade zero")
        void deveLancarExcecao_quandoBaixaQuantidadeZero() {
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);
            assertThatThrownBy(() -> peca.baixarEstoque(0))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao baixar estoque com quantidade nula")
        void deveLancarExcecao_quandoBaixaQuantidadeNula() {
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);
            assertThatThrownBy(() -> peca.baixarEstoque(null))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando estoque insuficiente")
        void deveLancarExcecao_quandoEstoqueInsuficiente() {
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 2);
            assertThatThrownBy(() -> peca.baixarEstoque(5))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("Sem estoque");
        }

        @Test
        @DisplayName("Deve repor estoque com sucesso")
        void deveReporEstoque_quandoQuantidadeValida() {
            // Arrange
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

            // Act
            peca.reporEstoque(5);

            // Assert
            assertThat(peca.getQuantidadeEstoque()).isEqualTo(15);
        }

        @Test
        @DisplayName("Deve lançar exceção ao repor estoque com quantidade zero")
        void deveLancarExcecao_quandoReposicaoQuantidadeZero() {
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);
            assertThatThrownBy(() -> peca.reporEstoque(0))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao repor estoque com quantidade nula")
        void deveLancarExcecao_quandoReposicaoQuantidadeNula() {
            Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);
            assertThatThrownBy(() -> peca.reporEstoque(null))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }
}
