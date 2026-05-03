package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto.PrestacaoServicoResponse;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrestacaoServicoResponseMapperTest {

    @Test
    @DisplayName("Deve mapear PrestacaoServico para response")
    void deveMapear_quandoPrestacaoValida() {
        // Arrange
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();

        // Act
        PrestacaoServicoResponse response = PrestacaoServicoResponseMapper.toResponse(ps);

        // Assert
        assertThat(response.id()).isEqualTo(ps.getId());
        assertThat(response.precoMaoDeObra()).isEqualTo(ps.getPrecoMaoDeObra());
    }
}
