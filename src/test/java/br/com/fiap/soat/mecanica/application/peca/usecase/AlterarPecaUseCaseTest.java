package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterarPecaUseCaseTest {

    @Mock
    private PecaRepository pecaRepository;
    @Mock
    private BuscarPecaPorIdUseCase buscarPecaPorIdUseCase;
    @InjectMocks
    private AlterarPecaUseCase useCase;

    @Test
    @DisplayName("Deve alterar peça com sucesso")
    void deveAlterar_quandoPecaExiste() {
        // Arrange
        Peca peca = TestDataFactory.criarPecaValida();
        when(buscarPecaPorIdUseCase.executar(any())).thenReturn(peca);
        when(pecaRepository.buscarPorNome(anyString())).thenReturn(Optional.empty());
        when(pecaRepository.salvar(any())).thenReturn(peca);

        // Act
        Peca resultado = useCase.executar(peca.getId(), "Disco", "Fremax", new BigDecimal("200"), 5);

        // Assert
        assertThat(resultado).isNotNull();
    }
}
