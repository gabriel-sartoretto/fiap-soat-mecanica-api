package br.com.fiap.soat.mecanica.adapters.in.web.servico;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.servico.*;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ServicoController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarServicoUseCase cadastrarUseCase;
    @MockitoBean
    private AlterarServicoUseCase alterarUseCase;
    @MockitoBean
    private AtivarServicoUseCase ativarUseCase;
    @MockitoBean
    private InativarServicoUseCase inativarUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve cadastrar serviço com role MECANICO")
    void deveCadastrar_quandoMecanico() throws Exception {
        Servico servico = TestDataFactory.criarServicoValido();
        when(cadastrarUseCase.executar(anyString(), anyString())).thenReturn(servico);

        String json = """
                {"nome":"Troca de Óleo","descricao":"Desc"}
                """;

        mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve alterar serviÃ§o")
    void deveAlterar() throws Exception {
        Servico servico = TestDataFactory.criarServicoValido();
        when(alterarUseCase.executar(any(), anyString(), anyString())).thenReturn(servico);

        String json = """
                {"nome":"Troca de Ã“leo","descricao":"DescriÃ§Ã£o alterada"}
                """;

        mockMvc.perform(put("/servicos/{id}", servico.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve ativar serviÃ§o")
    void deveAtivar() throws Exception {
        Servico servico = TestDataFactory.criarServicoValido();
        when(ativarUseCase.executar(any())).thenReturn(servico);

        mockMvc.perform(patch("/servicos/{id}/ativar", servico.getId()).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve inativar serviÃ§o")
    void deveInativar() throws Exception {
        Servico servico = TestDataFactory.criarServicoValido();
        when(inativarUseCase.executar(any())).thenReturn(servico);

        mockMvc.perform(patch("/servicos/{id}/inativar", servico.getId()).with(csrf()))
                .andExpect(status().isOk());
    }
}
