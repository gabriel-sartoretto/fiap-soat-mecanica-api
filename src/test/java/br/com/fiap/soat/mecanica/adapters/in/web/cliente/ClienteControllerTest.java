package br.com.fiap.soat.mecanica.adapters.in.web.cliente;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CustomUserDetailsService;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.cliente.usecase.*;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
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

@WebMvcTest(value = ClienteController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CadastrarClienteUseCase cadastrarUseCase;
    @MockitoBean
    private AlterarClienteUseCase alterarUseCase;
    @MockitoBean
    private BuscarClientePorCpfUseCase buscarCpfUseCase;
    @MockitoBean
    private BuscarClientePorCnpjUseCase buscarCnpjUseCase;
    @MockitoBean
    private BuscarClientePorUsuarioIdUseCase buscarUsuarioIdUseCase;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ATENDENTE")
    @DisplayName("Deve cadastrar cliente com role ATENDENTE")
    void deveCadastrar_quandoAtendente() throws Exception {
        Cliente cliente = TestDataFactory.criarClienteComCpf();
        when(cadastrarUseCase.executar(anyString(), anyString(), any(), anyString(), any())).thenReturn(cliente);

        String json = """
                {"nome":"Teste","cpf":"52998224725","email":"c@c.com"}
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    @DisplayName("Deve buscar cliente por CPF")
    void deveBuscarPorCpf() throws Exception {
        Cliente cliente = TestDataFactory.criarClienteComCpf();
        when(buscarCpfUseCase.executar(anyString())).thenReturn(cliente);

        mockMvc.perform(get("/clientes/por-cpf/{cpf}", "52998224725"))
                .andExpect(status().isOk());
    }
}
