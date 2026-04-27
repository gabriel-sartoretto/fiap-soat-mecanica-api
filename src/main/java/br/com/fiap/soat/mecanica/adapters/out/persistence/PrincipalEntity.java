package br.com.fiap.soat.mecanica.adapters.out.persistence;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@NoArgsConstructor
@MappedSuperclass
@JsonIgnoreProperties(value = {"dataCriacao", "criadoPor", "dataModificacao", "modificadoPor", "versao"})
@EntityListeners(AuditingEntityListener.class)
public abstract class PrincipalEntity implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected StatusRecursoEnum status = StatusRecursoEnum.ATIVO;
}
