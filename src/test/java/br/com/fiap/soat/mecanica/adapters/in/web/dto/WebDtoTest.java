package br.com.fiap.soat.mecanica.adapters.in.web.dto;

import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteAlterarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaAtualizarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.servico.dto.ServicoAlterarRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class WebDtoTest {

    @Test
    @DisplayName("Deve criar response de login com jwt")
    void deveCriarLoginResponse() {
        LoginResponse response = new LoginResponse("jwt-token");

        assertThat(response.jwt()).isEqualTo("jwt-token");
    }

    @Test
    @DisplayName("Deve criar request de alteraÃ§Ã£o de cliente")
    void deveCriarClienteAlterarRequest() {
        ClienteAlterarRequest request = new ClienteAlterarRequest("Cliente", "11999887766");

        assertThat(request.nome()).isEqualTo("Cliente");
        assertThat(request.telefone()).isEqualTo("11999887766");
    }

    @Test
    @DisplayName("Deve criar request de atualizaÃ§Ã£o de peÃ§a")
    void deveCriarPecaAtualizarRequest() {
        PecaAtualizarRequest request = new PecaAtualizarRequest(
                "Pastilha",
                "Bosch",
                new BigDecimal("180.00"),
                20
        );

        assertThat(request.nome()).isEqualTo("Pastilha");
        assertThat(request.marca()).isEqualTo("Bosch");
        assertThat(request.valorUnitario()).isEqualByComparingTo("180.00");
        assertThat(request.quantidadeEstoque()).isEqualTo(20);
    }

    @Test
    @DisplayName("Deve criar request de alteraÃ§Ã£o de serviÃ§o")
    void deveCriarServicoAlterarRequest() {
        ServicoAlterarRequest request = new ServicoAlterarRequest("Troca de Ã“leo", "DescriÃ§Ã£o");

        assertThat(request.nome()).isEqualTo("Troca de Ã“leo");
        assertThat(request.descricao()).isEqualTo("DescriÃ§Ã£o");
    }
}
