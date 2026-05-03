package br.com.fiap.soat.mecanica.adapters.in.web.veiculo;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.veiculo.usecase.*;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = VeiculoController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarVeiculoUseCase cadastrarUseCase;
    @MockitoBean
    private AlterarVeiculoUseCase alterarUseCase;
    @MockitoBean
    private BuscarVeiculoPorIdUseCase buscarPorIdUseCase;
    @MockitoBean
    private BuscarVeiculoPorPlacaUseCase buscarPorPlacaUseCase;
    @MockitoBean
    private BuscarTodosVeiculosPorClienteIdUseCase buscarTodosPorClienteUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    @DisplayName("Deve cadastrar veículo com role ATENDENTE")
    void deveCadastrar_quandoAtendente() throws Exception {
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        when(cadastrarUseCase.executar(anyString(), anyString(), anyString(), anyString(), any(Integer.class), any())).thenReturn(veiculo);

        String json = """
                {"placa":"ABC1234","marca":"Toyota","modelo":"Corolla","ano":"2023","quantidadeEixos":2,"clienteId":"%s"}
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    @DisplayName("Deve buscar veículo por placa")
    void deveBuscarPorPlaca() throws Exception {
        Veiculo veiculo = TestDataFactory.criarVeiculoValido();
        when(buscarPorPlacaUseCase.executar(anyString())).thenReturn(veiculo);

        mockMvc.perform(get("/veiculos/por-placa/{placa}", "ABC1234"))
                .andExpect(status().isOk());
    }
}
