package br.com.fiap.soat.mecanica.helper;

import java.util.UUID;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;

public abstract class ClienteHelper {

	public static Cliente gerarCliente() {
		var idCliente = UUID.randomUUID();
		CPF cpfCliente = new CPF("01234567890");
		var idUsuario = UUID.randomUUID();
		
		return Cliente.reconstruir(
				idCliente, 
				StatusRecursoEnum.ATIVO, 
				"Cliente Teste", 
				cpfCliente, 
				null, 
				null, 
				null, 
				idUsuario);
	}
}
