package br.com.fiap.soat.mecanica.adapters.in.web.veiculo.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto.VeiculoResponse;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoResponseMapperTest {

    @Test
    @DisplayName("Deve mapear Veiculo para VeiculoResponse")
    void deveMapear_quandoVeiculoValido() {
        // Arrange
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();

        // Act
        VeiculoResponse response = VeiculoResponseMapper.toResponse(veiculo);

        // Assert
        assertThat(response.id()).isEqualTo(veiculo.getId());
        assertThat(response.placa()).isEqualTo(veiculo.getPlaca().getValue());
        assertThat(response.marca()).isEqualTo(veiculo.getMarca());
    }
}
