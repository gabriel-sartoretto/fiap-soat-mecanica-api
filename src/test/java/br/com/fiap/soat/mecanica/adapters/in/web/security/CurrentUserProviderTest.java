package br.com.fiap.soat.mecanica.adapters.in.web.security;

import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserProviderTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private CurrentUserProvider provider;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve retornar usuário autenticado com sucesso")
    void deveRetornar_quandoAutenticado() {
        // Arrange
        UserDetails userDetails = new User("teste@email.com", "pass", List.of());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = provider.get();

        // Assert
        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não autenticado")
    void deveLancarExcecao_quandoNaoAutenticado() {
        assertThatThrownBy(() -> provider.get())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não autenticado");
    }
}
