package br.com.fiap.soat.mecanica.domain.cliente;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class Cliente extends Principal {

    private UUID id;
    private String nome;
    private CPF cpf;
    private CNPJ cnpj;
    private Email email;
    private Telefone telefone;
    private UUID usuarioId; // quem cadastrou relação fraca

    public Cliente(String nome, CPF cpf, CNPJ cnpj, Email email, Telefone telefone, UUID usuarioId) {
        if (nome == null) throw new RegraNegocioException("Nome obrigatório");
        if (usuarioId == null) throw new RegraNegocioException("Usuário que cadastrou é obrigatório");
        if (cpf == null && cnpj == null) {
            throw new RegraNegocioException("Cliente deve ter CPF ou CNPJ");
        }

        if (cpf != null && cnpj != null) {
            throw new RegraNegocioException("Cliente não pode ter CPF e CNPJ ao mesmo tempo");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.usuarioId = usuarioId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void alterar(String nome, Telefone telefone) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }

        if (telefone != null) {
            this.telefone = telefone;
        }
    }
}
