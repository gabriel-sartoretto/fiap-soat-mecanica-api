package br.com.fiap.soat.mecanica.domain;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Principal {

    protected StatusRecursoEnum status = StatusRecursoEnum.ATIVO;

    public void inativar() {
        setStatus(StatusRecursoEnum.INATIVO);
    }

    public void ativar() {
        setStatus(StatusRecursoEnum.ATIVO);
    }

    public boolean isAtivo() {
        return status.equals(StatusRecursoEnum.ATIVO);
    }

    public boolean isInativo() {
        return status.equals(StatusRecursoEnum.INATIVO);
    }
}

