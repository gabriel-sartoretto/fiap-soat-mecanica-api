package br.com.fiap.soat.mecanica.application.peca.usecase;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarPecaUseCaseTest {

    @Mock
    private PecaRepository pecaRepository;
    @InjectMocks
    private CadastrarPecaUseCase useCase;

    @Test
    @DisplayName("Deve cadastrar peça com sucesso")
    void deveCadastrar_quandoDadosValidos() {
        // Arrange
        when(pecaRepository.buscarPorNome(anyString())).thenReturn(Optional.empty());
        Peca peca = TestDataFactory.criarPecaValida();
        when(pecaRepository.salvar(any())).thenReturn(peca);

        // Act
        Peca resultado = useCase.executar("Pastilha", "Bosch", new BigDecimal("150"), 10);

        // Assert
        assertThat(resultado).isNotNull();
    }
}
