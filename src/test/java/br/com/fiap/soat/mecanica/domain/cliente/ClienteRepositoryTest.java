package br.com.fiap.soat.mecanica.domain.cliente;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.soat.mecanica.helper.ClienteHelper;

public class ClienteRepositoryTest {
	
	@Mock
	private ClienteRepository clienteRepository;
	
	AutoCloseable mock;
	
	@BeforeEach
	void setup() {
		mock = MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		mock.close();
	}
	
	@Test
	void devePermitirCadastrarCliente() {
		// Arrange - Preparar
		var cliente = ClienteHelper.gerarCliente();
		when(clienteRepository.salvar(any(Cliente.class))).thenReturn(cliente);
		
		//Act - Atuar
		var clienteArmazenado = clienteRepository.salvar(cliente);
		
		//Assert - Validar
		verify(clienteRepository, times(1)).salvar(cliente);
				
		assertThat(clienteArmazenado)
			.isInstanceOf(Cliente.class)
			.isNotNull()
			.isEqualTo(cliente);
	}
	
	@Test
	void devePermitirbuscarClientePorId() {
		// Arrange - Preparar
		var cliente = ClienteHelper.gerarCliente();
		var idCliente = cliente.getId();

		when(clienteRepository.buscarPorId(idCliente)).thenReturn(Optional.of(cliente));
		
		//Act - Atuar
		var clienteEncontrado = clienteRepository.buscarPorId(idCliente);
		
		//Assert - Validar
		verify(clienteRepository, times(1)).buscarPorId(idCliente);
				
		assertThat(clienteEncontrado)
			.isInstanceOf(Optional.class)
			.isNotNull()
			.containsSame(cliente);
	}

	@Test
	void devePermitirbuscarClientePorCPF() {
		fail("a fazer");
	}

	@Test
	void devePermitirbuscarClientePorCNPJ() {
		fail("a fazer");
	}

	@Test
	void devePermitirbuscarClientePorUsuarioID() {
		fail("a fazer");
	}
	
}
