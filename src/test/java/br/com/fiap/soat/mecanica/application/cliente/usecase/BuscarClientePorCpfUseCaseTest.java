package br.com.fiap.soat.mecanica.application.cliente.usecase;

import static org.assertj.core.api.Assertions.assertThat;
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

import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.helper.ClienteHelper;

public class BuscarClientePorCpfUseCaseTest {

	@Mock
	private ClienteRepository clienteRepository;
	
	private BuscarClientePorCpfUseCase buscarClientePorCpfUseCase;
	
	AutoCloseable mock;
	
	@BeforeEach
	void setup() {
		mock = MockitoAnnotations.openMocks(this);
		buscarClientePorCpfUseCase = new BuscarClientePorCpfUseCase(clienteRepository);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		mock.close();
	}
	
	@Test
	void devePermitirBuscarClientePorCPF() {
		//Arrange
		var cpfCliente = "01234567890";
		
		var cliente = ClienteHelper.gerarCliente();
        CPF cpf = cliente.getCpf();

		
		when(clienteRepository.buscarPorCpf(any(CPF.class))).thenReturn(Optional.of(cliente));
			
		//Act
		var clienteCadastrado = buscarClientePorCpfUseCase.executar(cpfCliente);
		
		//Assert
		verify(clienteRepository, times(0)).buscarPorCpf(cpf);
		
		assertThat(clienteCadastrado).isEqualTo(cliente);
	}
	
}
