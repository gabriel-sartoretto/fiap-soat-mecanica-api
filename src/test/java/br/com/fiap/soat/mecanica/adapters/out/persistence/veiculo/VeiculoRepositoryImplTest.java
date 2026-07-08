package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.mapper.VeiculoMapper;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.support.DatabaseTestFixtures;
import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({VeiculoRepositoryImpl.class, VeiculoMapper.class})
class VeiculoRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private VeiculoRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = DatabaseTestFixtures.insertVeiculoDependencies(jdbcTemplate).clienteId();
    }

    @Test
    @DisplayName("Deve salvar e buscar veiculo por placa")
    void deveSalvarEBuscarPorPlaca() {
        Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, clienteId);

        repository.salvar(veiculo);
        Optional<Veiculo> encontrado = repository.buscarPorPlaca("ABC1234");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getMarca()).isEqualTo("Toyota");
    }

    @Test
    @DisplayName("Deve buscar todos veiculos por cliente ID")
    void deveBuscarTodosPorClienteId() {
        Veiculo v1 = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, clienteId);
        Veiculo v2 = new Veiculo(new Placa("DEF5G67"), "Honda", "Civic", "2022", 2, clienteId);
        repository.salvar(v1);
        repository.salvar(v2);

        List<Veiculo> veiculos = repository.buscarTodosPorClienteId(clienteId);

        assertThat(veiculos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar veiculo por ID")
    void deveBuscarPorId() {
        Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, clienteId);
        Veiculo salvo = repository.salvar(veiculo);

        Optional<Veiculo> encontrado = repository.buscarPorId(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getPlaca().getValue()).isEqualTo("ABC1234");
    }
}
