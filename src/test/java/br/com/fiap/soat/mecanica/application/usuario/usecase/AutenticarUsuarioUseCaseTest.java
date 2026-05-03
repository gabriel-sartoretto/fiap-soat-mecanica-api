package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.exception.SenhaInvalidaException;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private PasswordEncoderPort encoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;
    @InjectMocks
    private AutenticarUsuarioUseCase useCase;

    @Test
    @DisplayName("Deve autenticar usuário com sucesso")
    void deveAutenticar_quandoSenhaValida() {
        // Arrange
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(buscarUsuarioPorEmailUseCase.executar(anyString())).thenReturn(usuario);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.gerarToken(anyString())).thenReturn("jwt-token");

        // Act
        String token = useCase.login("teste@email.com", "Senha@123");

        // Assert
        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha inválida")
    void deveLancarExcecao_quandoSenhaInvalida() {
        // Arrange
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(buscarUsuarioPorEmailUseCase.executar(anyString())).thenReturn(usuario);
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> useCase.login("teste@email.com", "senhaErrada"))
                .isInstanceOf(SenhaInvalidaException.class);
    }
}
