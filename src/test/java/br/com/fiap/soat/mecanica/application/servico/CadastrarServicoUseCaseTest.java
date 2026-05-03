package br.com.fiap.soat.mecanica.application.servico;

import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.servico.ServicoRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarServicoUseCaseTest {

    @Mock
    private ServicoRepository servicoRepository;
    @InjectMocks
    private CadastrarServicoUseCase useCase;

    @Test
    @DisplayName("Deve cadastrar serviço com sucesso")
    void deveCadastrar_quandoDadosValidos() {
        // Arrange
        when(servicoRepository.buscarPorNome(anyString())).thenReturn(Optional.empty());
        Servico servico = TestDataFactory.criarServicoValido();
        when(servicoRepository.salvar(any())).thenReturn(servico);

        // Act
        Servico resultado = useCase.executar("Troca de Óleo", "Desc");

        // Assert
        assertThat(resultado).isNotNull();
    }
}
