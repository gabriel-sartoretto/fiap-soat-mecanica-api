package br.com.fiap.soat.mecanica.helper;

import java.util.UUID;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;

public abstract class UsuarioHelper {

	public static Usuario gerarUsuario() {

		var idUsuario = UUID.randomUUID();
		var email = new Email("usuario@gmail.com");
		
		return Usuario.reconstruir(
				idUsuario, 
				StatusRecursoEnum.ATIVO, 
				"Usuario Teste", 
				"asdfasdfasdfasdfasdf", 
				email, 
				CargoEnum.ATENDENTE);
	}
}


/*
 * public static Usuario reconstruir( UUID id, StatusRecursoEnum status, String
 * nome, String senhaHash, Email email, CargoEnum cargoEnum ) { Usuario usuario
 * = new Usuario();
 * 
 * usuario.id = id; usuario.status = status; usuario.nome = nome; usuario.senha
 * = senhaHash; usuario.email = email; usuario.cargoEnum = cargoEnum;
 * 
 * return usuario; }
 */