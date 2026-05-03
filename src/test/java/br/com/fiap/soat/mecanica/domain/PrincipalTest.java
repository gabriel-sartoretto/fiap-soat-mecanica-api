package br.com.fiap.soat.mecanica.domain;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrincipalTest {

    private static class PrincipalConcreto extends Principal {}

    @Test
    @DisplayName("Deve iniciar com status ATIVO")
    void deveIniciarAtivo_quandoCriado() {
        // Arrange & Act
        PrincipalConcreto principal = new PrincipalConcreto();

        // Assert
        assertThat(principal.getStatus()).isEqualTo(StatusRecursoEnum.ATIVO);
        assertThat(principal.isAtivo()).isTrue();
        assertThat(principal.isInativo()).isFalse();
    }

    @Test
    @DisplayName("Deve inativar recurso")
    void deveInativar_quandoChamado() {
        // Arrange
        PrincipalConcreto principal = new PrincipalConcreto();

        // Act
        principal.inativar();

        // Assert
        assertThat(principal.getStatus()).isEqualTo(StatusRecursoEnum.INATIVO);
        assertThat(principal.isAtivo()).isFalse();
        assertThat(principal.isInativo()).isTrue();
    }

    @Test
    @DisplayName("Deve ativar recurso após inativar")
    void deveAtivar_quandoInativo() {
        // Arrange
        PrincipalConcreto principal = new PrincipalConcreto();
        principal.inativar();

        // Act
        principal.ativar();

        // Assert
        assertThat(principal.isAtivo()).isTrue();
        assertThat(principal.isInativo()).isFalse();
    }
}
