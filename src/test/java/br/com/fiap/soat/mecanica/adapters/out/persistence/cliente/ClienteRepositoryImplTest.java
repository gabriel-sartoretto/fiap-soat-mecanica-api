package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.mapper.ClienteMapper;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({ClienteRepositoryImpl.class, ClienteMapper.class})
class ClienteRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private ClienteRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar cliente por CPF")
    void deveSalvarEBuscarPorCpf() {
        Cliente cliente = new Cliente("Cliente Teste", new CPF("11144477735"), null,
                new Email("teste@email.com"), null, UUID.randomUUID());

        repository.salvar(cliente);
        Optional<Cliente> encontrado = repository.buscarPorCpf(new CPF("11144477735"));

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Cliente Teste");
    }

    @Test
    @DisplayName("Deve retornar vazio quando CPF não existe")
    void deveRetornarVazio_quandoCpfNaoExiste() {
        assertThat(repository.buscarPorCpf(new CPF("12345678909"))).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void deveBuscarPorId() {
        Cliente cliente = new Cliente("Cliente Teste", new CPF("39053344705"), null,
                new Email("id@email.com"), null, UUID.randomUUID());
        Cliente salvo = repository.salvar(cliente);

        Optional<Cliente> encontrado = repository.buscarPorId(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail().getValue()).isEqualTo("id@email.com");
    }

    @Test
    @DisplayName("Deve buscar cliente por CNPJ")
    void deveBuscarPorCnpj() {
        Cliente cliente = new Cliente("Cliente CNPJ", null, new CNPJ("04252011000110"),
                new Email("cnpj@email.com"), new Telefone("11999887766"), UUID.randomUUID());
        repository.salvar(cliente);

        Optional<Cliente> encontrado = repository.buscarPorCnpj(new CNPJ("04252011000110"));

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Cliente CNPJ");
    }

    @Test
    @DisplayName("Deve buscar clientes por usuário ID")
    void deveBuscarPorUsuarioId() {
        UUID usuarioId = UUID.randomUUID();
        repository.salvar(new Cliente("Cliente 1", new CPF("11144477735"), null,
                new Email("cliente1@email.com"), null, usuarioId));
        repository.salvar(new Cliente("Cliente 2", new CPF("12345678909"), null,
                new Email("cliente2@email.com"), null, usuarioId));

        assertThat(repository.buscarPorUsuarioId(usuarioId)).hasSize(2);
    }
}
