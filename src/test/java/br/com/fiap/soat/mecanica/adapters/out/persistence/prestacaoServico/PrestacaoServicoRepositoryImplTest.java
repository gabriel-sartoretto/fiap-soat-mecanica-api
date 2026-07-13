package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper.PrestacaoServicoMapper;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({PrestacaoServicoRepositoryImpl.class, PrestacaoServicoMapper.class})
class PrestacaoServicoRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private PrestacaoServicoRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID ordemServicoId;
    private UUID servicoId;

    @BeforeEach
    void setUp() {
        DatabaseTestFixtures.PrestacaoServicoDependencies dependencies =
                DatabaseTestFixtures.insertPrestacaoServicoDependencies(jdbcTemplate);
        ordemServicoId = dependencies.ordemServicoId();
        servicoId = dependencies.servicoId();
    }

    @Test
    @DisplayName("Deve salvar e buscar prestação por ID")
    void deveSalvarEBuscarPorId() {
        PrestacaoServico ps = new PrestacaoServico(new BigDecimal("200"), ordemServicoId, servicoId);

        PrestacaoServico salva = repository.salvar(ps);
        Optional<PrestacaoServico> encontrada = repository.buscarPorId(salva.getId());

        assertThat(encontrada).isPresent();
    }

    @Test
    @DisplayName("Deve buscar todas as prestações por OS ID")
    void deveBuscarTodosPorOsId() {
        UUID outroServicoId = DatabaseTestFixtures.insertServico(jdbcTemplate);
        repository.salvar(new PrestacaoServico(new BigDecimal("100"), ordemServicoId, servicoId));
        repository.salvar(new PrestacaoServico(new BigDecimal("200"), ordemServicoId, outroServicoId));

        List<PrestacaoServico> lista = repository.buscarTodosPorOrdemServicoId(ordemServicoId);

        assertThat(lista).hasSize(2);
    }

    @Test
    @DisplayName("Deve verificar existência por OS ID e serviço ID")
    void deveVerificarExistenciaPorOsIdEServicoId() {
        repository.salvar(new PrestacaoServico(new BigDecimal("100"), ordemServicoId, servicoId));

        assertThat(repository.existsByOrdemServicoIdAndServicoId(ordemServicoId, servicoId)).isTrue();
    }
}
