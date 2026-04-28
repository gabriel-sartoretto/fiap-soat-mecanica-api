package br.com.fiap.soat.mecanica.domain.cliente;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Cliente extends Principal {

    private UUID id;
    private String nome;
    private CPF cpf;
    private CNPJ cnpj;
    private Email email;
    private Telefone telefone;
    private UUID usuarioId; // quem cadastrou

    public Cliente(String nome, CPF cpf, CNPJ cnpj, Email email, Telefone telefone, UUID usuarioId) {
        if (nome == null) throw new RegraNegocioException("Nome obrigatório");
        this.nome = nome;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.usuarioId = usuarioId;
    }
}
