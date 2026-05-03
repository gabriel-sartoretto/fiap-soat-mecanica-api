package br.com.fiap.soat.mecanica.adapters.in.web.auth;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.usuario.usecase.AutenticarUsuarioUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AutenticarUsuarioUseCase autenticarUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser
    @DisplayName("Deve autenticar com sucesso e retornar token")
    void deveAutenticar_quandoCredenciaisValidas() throws Exception {
        // Arrange
        when(autenticarUseCase.login(anyString(), anyString())).thenReturn("jwt-token-123");

        String json = """
                {"email":"teste@email.com","senha":"Senha@123"}
                """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt-token-123"));
    }
}
