package br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaResponse;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PecaResponseMapperTest {

    @Test
    @DisplayName("Deve mapear Peca para PecaResponse")
    void deveMapear_quandoPecaValida() {
        // Arrange
        Peca peca = TestDataFactory.criarPecaValida();

        // Act
        PecaResponse response = PecaResponseMapper.toResponse(peca);

        // Assert
        assertThat(response.id()).isEqualTo(peca.getId());
        assertThat(response.nome()).isEqualTo(peca.getNome());
    }
}
