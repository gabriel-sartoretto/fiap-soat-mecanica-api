package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.prestacaoServico.*;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PrestacaoServicoController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class PrestacaoServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarPrestacaoServicoUseCase cadastrarUseCase;
    @MockitoBean
    private FinalizarPrestacaoServicoUseCase finalizarUseCase;
    @MockitoBean
    private InativarPrestacaoServicoUseCase inativarUseCase;
    @MockitoBean
    private BuscarTodosPrestacaoServicoProOrdemServicoIdUseCase buscarTodosUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve cadastrar prestação com role MECANICO")
    void deveCadastrar_quandoMecanico() throws Exception {
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        when(cadastrarUseCase.executar(any(), any(), any())).thenReturn(ps);

        String json = """
                {"precoMaoDeObra":200,"ordemServicoId":"%s","servicoId":"%s"}
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/prestacao-servico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve buscar prestações por OS ID")
    void deveBuscarPorOsId() throws Exception {
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
        when(buscarTodosUseCase.executar(any())).thenReturn(List.of(ps));

        mockMvc.perform(get("/prestacao-servico/por-ordem-servico/{osId}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve inativar prestacao de servico")
    void deveInativar() throws Exception {
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoInativa();
        when(inativarUseCase.executar(any())).thenReturn(ps);

        mockMvc.perform(patch("/prestacao-servico/{id}/inativar", ps.getId()).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve finalizar prestacao de servico")
    void deveFinalizar() throws Exception {
        PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoFinalizada();
        when(finalizarUseCase.executar(any())).thenReturn(ps);

        mockMvc.perform(patch("/prestacao-servico/{id}/finalizar", ps.getId()).with(csrf()))
                .andExpect(status().isOk());
    }
}
