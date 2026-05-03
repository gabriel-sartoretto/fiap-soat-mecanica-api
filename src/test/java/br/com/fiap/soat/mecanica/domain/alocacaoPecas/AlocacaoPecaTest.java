package br.com.fiap.soat.mecanica.domain.alocacaoPecas;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlocacaoPecaTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar AlocacaoPeca com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            AlocacaoPeca alocacao = new AlocacaoPeca(5, UUID.randomUUID(), UUID.randomUUID());

            // Assert
            assertThat(alocacao.getQuantidadeNecessaria()).isEqualTo(5);
            assertThat(alocacao.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando quantidade é nula")
        void deveLancarExcecao_quandoQuantidadeNula() {
            assertThatThrownBy(() -> new AlocacaoPeca(null, UUID.randomUUID(), UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando quantidade é zero")
        void deveLancarExcecao_quandoQuantidadeZero() {
            assertThatThrownBy(() -> new AlocacaoPeca(0, UUID.randomUUID(), UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando prestacaoServicoId é nulo")
        void deveLancarExcecao_quandoPrestacaoIdNulo() {
            assertThatThrownBy(() -> new AlocacaoPeca(5, null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando pecaId é nulo")
        void deveLancarExcecao_quandoPecaIdNulo() {
            assertThatThrownBy(() -> new AlocacaoPeca(5, UUID.randomUUID(), null))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruir")
    class Reconstruir {

        @Test
        @DisplayName("Deve reconstruir AlocacaoPeca com dados completos")
        void deveReconstruir_quandoDadosCompletos() {
            // Act
            AlocacaoPeca alocacao = br.com.fiap.soat.mecanica.util.TestDataFactory.criarAlocacaoPecaValida();

            // Assert
            assertThat(alocacao.getId()).isNotNull();
            assertThat(alocacao.isAtivo()).isTrue();
        }
    }
}
