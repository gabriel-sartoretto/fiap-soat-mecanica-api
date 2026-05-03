package br.com.fiap.soat.mecanica.adapters.in.web.cliente.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteResponse;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteResponseMapperTest {

    @Test
    @DisplayName("Deve mapear Cliente com CPF para response")
    void deveMapear_quandoClienteComCpf() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCpf();

        // Act
        ClienteResponse response = ClienteResponseMapper.toResponse(cliente);

        // Assert
        assertThat(response.id()).isEqualTo(cliente.getId());
        assertThat(response.cpf()).isNotNull();
        assertThat(response.cnpj()).isNull();
    }

    @Test
    @DisplayName("Deve mapear Cliente com CNPJ para response")
    void deveMapear_quandoClienteComCnpj() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCnpj();

        // Act
        ClienteResponse response = ClienteResponseMapper.toResponse(cliente);

        // Assert
        assertThat(response.id()).isEqualTo(cliente.getId());
        assertThat(response.cnpj()).isNotNull();
        assertThat(response.cpf()).isNull();
    }
}
