package br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto;

import java.util.UUID;
import jakarta.validation.constraints.*;

public record ClienteIncluirRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 255, message = "o nome deve ter entre 3 e 255 caracteres")
        String nome,

        String cpf,

        String cnpj,

        @NotBlank(message = "O e-mail não pode ser nulo ou vazio")
        @Email(message = "o e-mail é inválido")
        String email,

        String telefone
) {}
