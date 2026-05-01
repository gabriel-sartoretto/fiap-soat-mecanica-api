package br.com.fiap.soat.mecanica.domain.servico;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Servico extends Principal {

    private UUID id;
    private String nome;
    private String descricao;

    public Servico(String nome, String descricao) {
        validar(nome);
        this.nome = nome;
        this.descricao = descricao;
    }

    public static Servico reconstruir(
            UUID id,
            StatusRecursoEnum status,
            String nome,
            String descricao
    ) {
        Servico servico = new Servico();
        servico.id = id;
        servico.status = status;
        servico.nome = nome;
        servico.descricao = descricao;
        return servico;
    }

    public void alterar(String nome, String descricao) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }

        this.descricao = descricao;
    }

    private void validar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraNegocioException("Nome do serviço é obrigatório");
        }
    }
}
