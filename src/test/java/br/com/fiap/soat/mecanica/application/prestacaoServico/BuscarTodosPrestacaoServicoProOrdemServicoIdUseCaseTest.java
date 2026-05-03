package br.com.fiap.soat.mecanica.application.prestacaoServico;

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
class BuscarTodosPrestacaoServicoProOrdemServicoIdUseCaseTest {

    @Mock
    private PrestacaoServicoRepository prestacaoServicoRepository;
    @InjectMocks
    private BuscarTodosPrestacaoServicoProOrdemServicoIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar todas prestações por OS ID")
    void deveBuscar_quandoOsIdValido() {
        // Arrange
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        when(prestacaoServicoRepository.buscarTodosPorOrdemServicoId(any())).thenReturn(List.of(ps));

        // Act
        List<PrestacaoServico> resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado).hasSize(1);
    }
}
