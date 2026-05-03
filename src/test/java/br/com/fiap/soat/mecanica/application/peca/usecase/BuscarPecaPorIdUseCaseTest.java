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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPecaPorIdUseCaseTest {

    @Mock
    private PecaRepository pecaRepository;
    @InjectMocks
    private BuscarPecaPorIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar peça por ID com sucesso")
    void deveBuscar_quandoPecaExiste() {
        // Arrange
        Peca peca = TestDataFactory.criarPecaValida();
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.of(peca));

        // Act
        Peca resultado = useCase.executar(peca.getId());

        // Assert
        assertThat(resultado).isEqualTo(peca);
    }

    @Test
    @DisplayName("Deve lançar exceção quando peça não encontrada")
    void deveLancarExcecao_quandoNaoEncontrada() {
        when(pecaRepository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(UUID.randomUUID()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
