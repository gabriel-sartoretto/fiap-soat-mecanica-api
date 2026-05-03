package br.com.fiap.soat.mecanica.adapters.in.web.security;

import br.com.fiap.soat.mecanica.application.usuario.usecase.BuscarUsuarioPorEmailUseCase;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private BuscarUsuarioPorEmailUseCase buscarUseCase;
    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    @DisplayName("Deve carregar usuário por email com sucesso")
    void deveCarregar_quandoEmailValido() {
        // Arrange
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(buscarUseCase.executar(anyString())).thenReturn(usuario);

        // Act
        UserDetails userDetails = service.loadUserByUsername("teste@email.com");

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(usuario.getEmail().getValue());
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }
}
