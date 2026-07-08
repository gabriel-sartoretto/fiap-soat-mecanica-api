package br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico.mapper.OrdemServicoMapper;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
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
@Import({OrdemServicoRepositoryImpl.class, OrdemServicoMapper.class})
class OrdemServicoRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private OrdemServicoRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID veiculoId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        DatabaseTestFixtures.OrdemServicoDependencies dependencies =
                DatabaseTestFixtures.insertOrdemServicoDependencies(jdbcTemplate);
        veiculoId = dependencies.veiculoId();
        usuarioId = dependencies.usuarioId();
    }

    @Test
    @DisplayName("Deve salvar e buscar OS por ID")
    void deveSalvarEBuscarPorId() {
        OrdemServico os = new OrdemServico("Observacao teste", veiculoId, usuarioId);

        OrdemServico salva = repository.salvar(os);
        Optional<OrdemServico> encontrada = repository.buscarPorId(salva.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getObservacao()).isEqualTo("Observacao teste");
    }

    @Test
    @DisplayName("Deve buscar todas OS por veiculo ID")
    void deveBuscarTodosPorVeiculoId() {
        repository.salvar(new OrdemServico("OS 1", veiculoId, usuarioId));
        repository.salvar(new OrdemServico("OS 2", veiculoId, usuarioId));

        List<OrdemServico> lista = repository.buscarTodosPorVeiculoId(veiculoId);

        assertThat(lista).hasSize(2);
    }
}
