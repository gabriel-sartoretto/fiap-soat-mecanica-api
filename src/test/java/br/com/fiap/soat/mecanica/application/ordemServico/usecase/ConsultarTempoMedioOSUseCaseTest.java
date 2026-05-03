package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarTempoMedioOSUseCaseTest {

    @Mock
    private PrestacaoServicoRepository prestacaoRepository;
    @InjectMocks
    private ConsultarTempoMedioOSUseCase useCase;

    @Test
    @DisplayName("Deve consultar tempo médio com sucesso")
    void deveConsultar_quandoComPrestacoes() {
        // Arrange
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoFinalizada();
        TempoMedioServicoResult item = new TempoMedioServicoResult("Troca de Óleo", 3600.0);
        when(prestacaoRepository.buscarTodosPorOrdemServicoId(any())).thenReturn(List.of(ps));
        when(prestacaoRepository.calcularTempoMedioPorServicos(any())).thenReturn(List.of(item));

        // Act
        TempoMedioOSResult resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado.itens()).hasSize(1);
        assertThat(resultado.tempoTotalSegundos()).isEqualTo(3600.0);
    }

    @Test
    @DisplayName("Deve retornar tempo zero quando sem prestações")
    void deveRetornarZero_quandoSemPrestacoes() {
        // Arrange
        when(prestacaoRepository.buscarTodosPorOrdemServicoId(any())).thenReturn(List.of());
        when(prestacaoRepository.calcularTempoMedioPorServicos(any())).thenReturn(List.of());

        // Act
        TempoMedioOSResult resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado.tempoTotalSegundos()).isEqualTo(0.0);
    }
}
