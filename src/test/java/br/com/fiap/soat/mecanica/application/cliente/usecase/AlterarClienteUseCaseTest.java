package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
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
class AlterarClienteUseCaseTest {

    @Mock
    private ClienteRepository repository;
    @InjectMocks
    private AlterarClienteUseCase useCase;

    @Test
    @DisplayName("Deve alterar cliente com sucesso")
    void deveAlterar_quandoClienteExiste() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCpf();
        when(repository.buscarPorId(any())).thenReturn(Optional.of(cliente));
        when(repository.salvar(any())).thenReturn(cliente);

        // Act
        Cliente resultado = useCase.executar(cliente.getId(), "Novo Nome", "11988776655");

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado")
    void deveLancarExcecao_quandoNaoEncontrado() {
        when(repository.buscarPorId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar(UUID.randomUUID(), "Nome", null))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
