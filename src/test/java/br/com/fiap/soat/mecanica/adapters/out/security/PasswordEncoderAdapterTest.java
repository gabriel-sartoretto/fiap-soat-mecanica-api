package br.com.fiap.soat.mecanica.adapters.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderAdapterTest {

    private PasswordEncoderAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PasswordEncoderAdapter();
    }

    @Test
    @DisplayName("Deve codificar senha")
    void deveCodificar_quandoSenhaValida() {
        // Act
        String encoded = adapter.encode("Senha@123");

        // Assert
        assertThat(encoded).isNotNull().startsWith("$2a$");
    }

    @Test
    @DisplayName("Deve retornar true quando senha corresponde")
    void deveRetornarTrue_quandoSenhaCorresponde() {
        // Arrange
        String encoded = adapter.encode("Senha@123");

        // Act & Assert
        assertThat(adapter.matches("Senha@123", encoded)).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando senha não corresponde")
    void deveRetornarFalse_quandoSenhaNaoCorresponde() {
        // Arrange
        String encoded = adapter.encode("Senha@123");

        // Act & Assert
        assertThat(adapter.matches("SenhaErrada", encoded)).isFalse();
    }
}
