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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarClientePorCpfUseCaseTest {

    @Mock
    private ClienteRepository repository;
    @InjectMocks
    private BuscarClientePorCpfUseCase useCase;

    @Test
    @DisplayName("Deve buscar cliente por CPF com sucesso")
    void deveBuscar_quandoCpfExiste() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCpf();
        when(repository.buscarPorCpf(any())).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = useCase.executar("52998224725");

        // Assert
        assertThat(resultado).isEqualTo(cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado por CPF")
    void deveLancarExcecao_quandoNaoEncontrado() {
        when(repository.buscarPorCpf(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar("52998224725"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
