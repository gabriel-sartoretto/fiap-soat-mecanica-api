package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.TempoMedioOSResponse;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TempoMedioOSResponseMapperTest {

    @Test
    @DisplayName("Deve mapear TempoMedioOSResult para TempoMedioOSResponse")
    void deveMapear_quandoResultValido() {
        // Arrange
        TempoMedioServicoResult item = new TempoMedioServicoResult("Troca de Óleo", 3600.0);
        TempoMedioOSResult result = new TempoMedioOSResult(List.of(item), 3600.0);

        // Act
        TempoMedioOSResponse response = TempoMedioOSResponseMapper.toResponse(result);

        // Assert
        assertThat(response.servicos()).hasSize(1);
        assertThat(response.tempoTotal()).isEqualTo("1h 0min");
    }

    @Test
    @DisplayName("Deve retornar null quando result é null")
    void deveRetornarNull_quandoResultNull() {
        assertThat(TempoMedioOSResponseMapper.toResponse(null)).isNull();
    }

    @Test
    @DisplayName("Deve formatar tempo em minutos quando menos de 1 hora")
    void deveFormatarMinutos_quandoMenosDeUmaHora() {
        // Arrange
        TempoMedioServicoResult item = new TempoMedioServicoResult("Serviço", 1800.0);
        TempoMedioOSResult result = new TempoMedioOSResult(List.of(item), 1800.0);

        // Act
        TempoMedioOSResponse response = TempoMedioOSResponseMapper.toResponse(result);

        // Assert
        assertThat(response.tempoTotal()).isEqualTo("30 min");
    }

    @Test
    @DisplayName("Deve exibir 'Sem histórico' quando tempo null")
    void deveExibirSemHistorico_quandoTempoNull() {
        // Arrange
        TempoMedioServicoResult item = new TempoMedioServicoResult("Serviço", null);
        TempoMedioOSResult result = new TempoMedioOSResult(List.of(item), 0.0);

        // Act
        TempoMedioOSResponse response = TempoMedioOSResponseMapper.toResponse(result);

        // Assert
        assertThat(response.servicos().get(0).tempoMedio()).isEqualTo("Sem histórico");
    }
}
