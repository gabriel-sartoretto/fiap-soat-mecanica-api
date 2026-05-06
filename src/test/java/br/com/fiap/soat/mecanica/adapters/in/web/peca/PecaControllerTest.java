package br.com.fiap.soat.mecanica.adapters.in.web.peca;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.peca.usecase.*;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
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

@WebMvcTest(value = PecaController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class PecaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarPecaUseCase cadastrarUseCase;
    @MockitoBean
    private AlterarPecaUseCase alterarUseCase;
    @MockitoBean
    private BuscarPecaPorIdUseCase buscarPorIdUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ALMOXARIFE")
    @DisplayName("Deve cadastrar peça com role ALMOXARIFE")
    void deveCadastrar_quandoAlmoxarife() throws Exception {
        Peca peca = TestDataFactory.criarPecaValida();
        when(cadastrarUseCase.executar(anyString(), anyString(), any(), any(Integer.class))).thenReturn(peca);

        String json = """
                {"nome":"Pastilha","marca":"Bosch","valorUnitario":150,"quantidadeEstoque":10}
                """;

        mockMvc.perform(post("/pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ALMOXARIFE")
    @DisplayName("Deve buscar peça por ID")
    void deveBuscarPorId() throws Exception {
        Peca peca = TestDataFactory.criarPecaValida();
        when(buscarPorIdUseCase.executar(any())).thenReturn(peca);

        mockMvc.perform(get("/pecas/{id}", peca.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ALMOXARIFE")
    @DisplayName("Deve alterar peca com role ALMOXARIFE")
    void deveAlterar_quandoAlmoxarife() throws Exception {
        Peca peca = TestDataFactory.criarPecaValida();
        when(alterarUseCase.executar(any(), anyString(), anyString(), any(), any(Integer.class))).thenReturn(peca);

        String json = """
                {"nome":"Pastilha","marca":"Bosch","valorUnitario":180,"quantidadeEstoque":20}
                """;

        mockMvc.perform(put("/pecas/{id}", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }
}
