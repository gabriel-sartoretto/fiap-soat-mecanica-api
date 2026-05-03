package br.com.fiap.soat.mecanica.adapters.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("dGVzdC1zZWNyZXQta2V5LXRoYXQtaXMtbG9uZy1lbm91Z2gtZm9yLWhzNTEyLWFsZ29yaXRobS10ZXN0aW5n");
    }

    @Test
    @DisplayName("Deve gerar token JWT válido")
    void deveGerarToken_quandoEmailValido() {
        // Act
        String token = jwtService.gerarToken("teste@email.com");

        // Assert
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Deve extrair username do token")
    void deveExtrairUsername_quandoTokenValido() {
        // Arrange
        String token = jwtService.gerarToken("teste@email.com");

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertThat(username).isEqualTo("teste@email.com");
    }

    @Test
    @DisplayName("Deve validar token como válido")
    void deveValidarToken_quandoTokenValido() {
        // Arrange
        String token = jwtService.gerarToken("teste@email.com");
        UserDetails userDetails = new User("teste@email.com", "pass", List.of());

        // Act
        boolean valid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("Deve invalidar token quando username diferente")
    void deveInvalidarToken_quandoUsernameDiferente() {
        // Arrange
        String token = jwtService.gerarToken("teste@email.com");
        UserDetails userDetails = new User("outro@email.com", "pass", List.of());

        // Act
        boolean valid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertThat(valid).isFalse();
    }
}
