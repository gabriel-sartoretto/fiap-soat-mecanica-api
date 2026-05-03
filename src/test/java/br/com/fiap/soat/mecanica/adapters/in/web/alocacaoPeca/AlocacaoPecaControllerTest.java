package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.alocacaoPeca.CadastrarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.application.alocacaoPeca.InativarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AlocacaoPecaController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class AlocacaoPecaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarAlocacaoPecaUseCase cadastrarUseCase;
    @MockitoBean
    private InativarAlocacaoPecaUseCase inativarUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ALMOXARIFE")
    @DisplayName("Deve cadastrar alocação com role ALMOXARIFE")
    void deveCadastrar_quandoAlmoxarife() throws Exception {
        AlocacaoPeca al = TestDataFactory.criarAlocacaoPecaValida();
        when(cadastrarUseCase.executar(any(Integer.class), any(), any())).thenReturn(al);

        String json = """
                {"quantidadeNecessaria":2,"prestacaoServicoId":"%s","pecaId":"%s"}
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/alocacao-pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }
}
