package br.com.fiap.soat.mecanica.domain.servico;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServicoTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar Servico com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            Servico servico = new Servico("Troca de Óleo", "Troca completa");

            // Assert
            assertThat(servico.getNome()).isEqualTo("Troca de Óleo");
            assertThat(servico.getDescricao()).isEqualTo("Troca completa");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é nulo")
        void deveLancarExcecao_quandoNomeNulo() {
            assertThatThrownBy(() -> new Servico(null, "Descrição"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Nome do serviço é obrigatório");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é vazio")
        void deveLancarExcecao_quandoNomeVazio() {
            assertThatThrownBy(() -> new Servico("  ", "Descrição"))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Nome do serviço é obrigatório");
        }
    }

    @Nested
    @DisplayName("Alterar")
    class Alterar {

        @Test
        @DisplayName("Deve alterar nome e descrição")
        void deveAlterar_quandoDadosValidos() {
            // Arrange
            Servico servico = new Servico("Troca de Óleo", "Troca completa");

            // Act
            servico.alterar("Alinhamento", "Alinhamento e balanceamento");

            // Assert
            assertThat(servico.getNome()).isEqualTo("Alinhamento");
            assertThat(servico.getDescricao()).isEqualTo("Alinhamento e balanceamento");
        }
    }
}
