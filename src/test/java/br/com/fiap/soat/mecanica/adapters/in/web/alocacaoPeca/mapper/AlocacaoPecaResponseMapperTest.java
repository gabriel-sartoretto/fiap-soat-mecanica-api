package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaResponse;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlocacaoPecaResponseMapperTest {

    @Test
    @DisplayName("Deve mapear AlocacaoPeca para response")
    void deveMapear_quandoAlocacaoValida() {
        // Arrange
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaValida();

        // Act
        AlocacaoPecaResponse response = AlocacaoPecaResponseMapper.toResponse(al);

        // Assert
        assertThat(response.id()).isEqualTo(al.getId());
        assertThat(response.quantidadeNecessaria()).isEqualTo(al.getQuantidadeNecessaria());
    }
}
