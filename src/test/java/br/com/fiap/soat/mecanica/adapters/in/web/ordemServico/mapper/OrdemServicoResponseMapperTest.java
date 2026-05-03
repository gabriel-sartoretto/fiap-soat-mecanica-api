package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.OrdemServicoResponse;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrdemServicoResponseMapperTest {

    @Test
    @DisplayName("Deve mapear OrdemServico para OrdemServicoResponse")
    void deveMapear_quandoOsValida() {
        // Arrange
        OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();

        // Act
        OrdemServicoResponse response = OrdemServicoResponseMapper.toResponse(os);

        // Assert
        assertThat(response.id()).isEqualTo(os.getId());
        assertThat(response.situacao()).isEqualTo(os.getSituacao());
        assertThat(response.observacao()).isEqualTo(os.getObservacao());
    }
}
