package br.com.fiap.soat.mecanica.adapters.in.web.usuario.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto.UsuarioResponse;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioResponseMapperTest {

    @Test
    @DisplayName("Deve mapear Usuario para UsuarioResponse")
    void deveMapear_quandoUsuarioValido() {
        // Arrange
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();

        // Act
        UsuarioResponse response = UsuarioResponseMapper.toResponse(usuario);

        // Assert
        assertThat(response.id()).isEqualTo(usuario.getId());
        assertThat(response.nome()).isEqualTo(usuario.getNome());
        assertThat(response.cargoEnum()).isEqualTo(usuario.getCargoEnum());
    }
}
