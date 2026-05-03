package br.com.fiap.soat.mecanica.adapters.in.web.servico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.servico.dto.ServicoResponse;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServicoResponseMapperTest {

    @Test
    @DisplayName("Deve mapear Servico para ServicoResponse")
    void deveMapear_quandoServicoValido() {
        // Arrange
        Servico servico = TestDataFactory.criarServicoValido();

        // Act
        ServicoResponse response = ServicoResponseMapper.toResponse(servico);

        // Assert
        assertThat(response.id()).isEqualTo(servico.getId());
        assertThat(response.nome()).isEqualTo(servico.getNome());
    }
}
