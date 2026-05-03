package br.com.fiap.soat.mecanica.domain.usuario;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar Usuario com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            Usuario usuario = new Usuario("Teste", "$2a$10$hash", new Email("teste@email.com"), CargoEnum.MECANICO);

            // Assert
            assertThat(usuario.getNome()).isEqualTo("Teste");
            assertThat(usuario.getCargoEnum()).isEqualTo(CargoEnum.MECANICO);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é nulo")
        void deveLancarExcecao_quandoNomeNulo() {
            assertThatThrownBy(() -> new Usuario(null, "$2a$10$hash", new Email("t@t.com"), CargoEnum.MECANICO))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando senhaHash é nula")
        void deveLancarExcecao_quandoSenhaHashNula() {
            assertThatThrownBy(() -> new Usuario("Teste", null, new Email("t@t.com"), CargoEnum.MECANICO))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando email é nulo")
        void deveLancarExcecao_quandoEmailNulo() {
            assertThatThrownBy(() -> new Usuario("Teste", "$2a$10$hash", null, CargoEnum.MECANICO))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando cargo é nulo")
        void deveLancarExcecao_quandoCargoNulo() {
            assertThatThrownBy(() -> new Usuario("Teste", "$2a$10$hash", new Email("t@t.com"), null))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruir")
    class Reconstruir {

        @Test
        @DisplayName("Deve reconstruir Usuario com dados completos")
        void deveReconstruir_quandoDadosCompletos() {
            // Act
            Usuario usuario = TestDataFactory.criarUsuarioMecanico();

            // Assert
            assertThat(usuario.getId()).isNotNull();
            assertThat(usuario.isAtivo()).isTrue();
        }
    }
}
