package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.ordemServico.usecase.*;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
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

@WebMvcTest(value = OrdemServicoController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class OrdemServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarOrdemServicoUseCase cadastrarUseCase;
    @MockitoBean
    private BuscarOrdemServicoPorIdUseCase buscarPorIdUseCase;
    @MockitoBean
    private EnviarOrdemServicoParaAprovacaoUseCase enviarAprovacaoUseCase;
    @MockitoBean
    private IniciarExecucaoOrdemServicoUseCase iniciarExecucaoUseCase;
    @MockitoBean
    private VoltarOrdemServicoParaDiagnosticoUseCase voltarDiagnosticoUseCase;
    @MockitoBean
    private CancelarOrdemServicoPorDesistenciaUseCase cancelarUseCase;
    @MockitoBean
    private PagarEEntregarOrdemServicoUseCase pagarEntregarUseCase;
    @MockitoBean
    private ConsultarTempoMedioOSUseCase consultarTempoMedioUseCase;
    @MockitoBean
    private BuscarTodosOrdemServicoPorVeiculoPlacaUseCase buscarTodosPorPlacaUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve cadastrar OS com role MECANICO")
    void deveCadastrar_quandoMecanico() throws Exception {
        // Arrange
        OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
        when(cadastrarUseCase.executar(anyString(), any())).thenReturn(os);

        String json = """
                {"observacao":"Teste","veiculoId":"%s"}
                """.formatted(UUID.randomUUID());

        // Act & Assert
        mockMvc.perform(post("/ordem-servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve buscar OS por ID")
    void deveBuscarPorId() throws Exception {
        OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
        when(buscarPorIdUseCase.executar(any())).thenReturn(os);

        mockMvc.perform(get("/ordem-servicos/{id}", os.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MECANICO")
    @DisplayName("Deve buscar todas OS por placa do veículo")
    void deveBuscarTodosPorVeiculoPlaca() throws Exception {
        OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
        when(buscarTodosPorPlacaUseCase.executar(anyString())).thenReturn(List.of(os));

        mockMvc.perform(get("/ordem-servicos/veiculo/placa/{placa}", "ABC1234"))
                .andExpect(status().isOk());
    }
}
