package br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioIncluirRequest(

        @NotBlank(message = "O nome não pode ser nulo ou vazio")
        String nome,

        @Email
        @NotBlank(message = "O e-mail não pode ser nulo ou vazio")
        String email,

        @NotBlank(message = "A senha não pode ser nulo ou vazio")
        String senha,

        @NotNull(message = "O cargo não pode ser nulo")
        @Schema(allowableValues = "ATENDENTE, MECANICO, ALMOXARIFE")
        CargoEnum cargoEnum
) {}
