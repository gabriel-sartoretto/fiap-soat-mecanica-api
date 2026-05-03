package br.com.fiap.soat.mecanica.application.cliente.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CurrentUserProvider;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.helper.UsuarioHelper;

public class CadastrarClienteUseCaseTest {

	@Mock
	private ClienteRepository clienteRepository;
	
	@Mock
	private CurrentUserProvider currentUser;
	
	private CadastrarClienteUseCase cadastrarClienteUseCase;
	
	AutoCloseable mock;
	
	@BeforeEach
	void setup() {
		mock = MockitoAnnotations.openMocks(this);
		cadastrarClienteUseCase = new CadastrarClienteUseCase(clienteRepository, currentUser);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		mock.close();
	}
	
	@Test
	void devePermitirCadastrarCliente() {
		//Arrange
		var usuario = UsuarioHelper.gerarUsuario();
		var nomeCliente = "Cliente Teste";
		var cpfCliente = "01234567890";
		var emailCliente = "teste@gmail.com";
		var telCliente = "(11)99999-1111";
		
		when(clienteRepository.salvar(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
		
		when(currentUser.get()).thenReturn(usuario);
		
		//Act
		var clienteCadastrado = cadastrarClienteUseCase.executar(nomeCliente, cpfCliente, null, emailCliente, telCliente);
		
		//Assert
		assertThat(clienteCadastrado)
			.isNotNull()
			.isInstanceOf(Cliente.class);
		
		assertThat(clienteCadastrado.getNome())
		.isEqualTo(nomeCliente);
		
	}
	
}
