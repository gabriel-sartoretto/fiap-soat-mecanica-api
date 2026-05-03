package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
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
class BuscarClientePorUsuarioIdUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private BuscarClientePorUsuarioIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar clientes por usuario ID")
    void deveBuscar_quandoUsuarioIdValido() {
        // Arrange
        Cliente cliente = TestDataFactory.criarClienteComCpf();
        when(clienteRepository.buscarPorUsuarioId(any())).thenReturn(List.of(cliente));

        // Act
        List<Cliente> resultado = useCase.executar(UUID.randomUUID());

        // Assert
        assertThat(resultado).hasSize(1);
    }
}
