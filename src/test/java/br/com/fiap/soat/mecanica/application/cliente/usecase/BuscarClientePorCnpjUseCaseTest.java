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
class BuscarClientePorCnpjUseCaseTest {

    @Mock
    private ClienteRepository repository;
    @InjectMocks
    private BuscarClientePorCnpjUseCase useCase;

    @Test
    @DisplayName("Deve buscar cliente por CNPJ com sucesso")
    void deveBuscar_quandoCnpjExiste() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCnpj();
        when(repository.buscarPorCnpj(any())).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = useCase.executar("11222333000181");

        // Assert
        assertThat(resultado).isEqualTo(cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado por CNPJ")
    void deveLancarExcecao_quandoNaoEncontrado() {
        when(repository.buscarPorCnpj(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar("11222333000181"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
