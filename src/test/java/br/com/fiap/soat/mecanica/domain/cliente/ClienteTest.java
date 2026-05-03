package br.com.fiap.soat.mecanica.domain.cliente;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClienteTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar Cliente com CPF")
        void deveCriar_quandoComCpf() {
            // Arrange & Act
            Cliente cliente = new Cliente("Nome", new CPF("52998224725"), null,
                    new Email("teste@email.com"), new Telefone("11999887766"), UUID.randomUUID());

            // Assert
            assertThat(cliente.getNome()).isEqualTo("Nome");
            assertThat(cliente.getCpf()).isNotNull();
            assertThat(cliente.getCnpj()).isNull();
        }

        @Test
        @DisplayName("Deve criar Cliente com CNPJ")
        void deveCriar_quandoComCnpj() {
            // Arrange & Act
            Cliente cliente = new Cliente("Nome", null, new CNPJ("11222333000181"),
                    new Email("teste@email.com"), null, UUID.randomUUID());

            // Assert
            assertThat(cliente.getCpf()).isNull();
            assertThat(cliente.getCnpj()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção quando sem CPF e sem CNPJ")
        void deveLancarExcecao_quandoSemCpfESemCnpj() {
            assertThatThrownBy(() -> new Cliente("Nome", null, null,
                    new Email("teste@email.com"), null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Cliente deve ter CPF ou CNPJ");
        }

        @Test
        @DisplayName("Deve lançar exceção quando com CPF e CNPJ")
        void deveLancarExcecao_quandoComCpfECnpj() {
            assertThatThrownBy(() -> new Cliente("Nome", new CPF("52998224725"),
                    new CNPJ("11222333000181"), new Email("teste@email.com"), null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Cliente não pode ter CPF e CNPJ ao mesmo tempo");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é nulo")
        void deveLancarExcecao_quandoNomeNulo() {
            assertThatThrownBy(() -> new Cliente(null, new CPF("52998224725"), null,
                    new Email("teste@email.com"), null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Nome obrigatório");
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuarioId é nulo")
        void deveLancarExcecao_quandoUsuarioIdNulo() {
            assertThatThrownBy(() -> new Cliente("Nome", new CPF("52998224725"), null,
                    new Email("teste@email.com"), null, null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessage("Usuário que cadastrou é obrigatório");
        }
    }

    @Nested
    @DisplayName("Alterar")
    class Alterar {

        @Test
        @DisplayName("Deve alterar nome e telefone")
        void deveAlterar_quandoDadosValidos() {
            // Arrange
            Cliente cliente = new Cliente("Nome", new CPF("52998224725"), null,
                    new Email("teste@email.com"), new Telefone("11999887766"), UUID.randomUUID());

            // Act
            cliente.alterar("Novo Nome", new Telefone("11988776655"));

            // Assert
            assertThat(cliente.getNome()).isEqualTo("Novo Nome");
        }
    }
}
