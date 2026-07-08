package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.mapper.AlocacaoPecaMapper;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({AlocacaoPecaRepositoryImpl.class, AlocacaoPecaMapper.class})
class AlocacaoPecaRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private AlocacaoPecaRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID prestacaoServicoId;
    private UUID pecaId;

    @BeforeEach
    void setUp() {
        DatabaseTestFixtures.AlocacaoPecaDependencies dependencies =
                DatabaseTestFixtures.insertAlocacaoPecaDependencies(jdbcTemplate);
        prestacaoServicoId = dependencies.prestacaoServicoId();
        pecaId = dependencies.pecaId();
    }

    @Test
    @DisplayName("Deve salvar e buscar alocacao por ID")
    void deveSalvarEBuscarPorId() {
        AlocacaoPeca alocacao = new AlocacaoPeca(2, prestacaoServicoId, pecaId);

        AlocacaoPeca salva = repository.salvar(alocacao);
        Optional<AlocacaoPeca> encontrada = repository.buscarPorId(salva.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getQuantidadeNecessaria()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve verificar alocacao ativa por prestacao e peca")
    void deveVerificarExistenciaPorPrestacaoServicoEPeca() {
        repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, pecaId));

        assertThat(repository.existsByPrestacaoServicoIdAndPecaId(prestacaoServicoId, pecaId)).isTrue();
    }

    @Test
    @DisplayName("Deve buscar todas alocacoes por prestacao de servico")
    void deveBuscarTodosPorPrestacaoServicoId() {
        UUID outraPecaId = DatabaseTestFixtures.insertPeca(jdbcTemplate);
        repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, pecaId));
        repository.salvar(new AlocacaoPeca(3, prestacaoServicoId, outraPecaId));

        assertThat(repository.buscarTodosPorPrestacaoServicoId(prestacaoServicoId)).hasSize(2);
    }

    @Test
    @DisplayName("Nao deve considerar alocacao inativa como existente")
    void naoDeveConsiderarAlocacaoInativaComoExistente() {
        AlocacaoPeca inativa = repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, pecaId));
        inativa.inativar();
        repository.salvar(inativa);

        assertThat(repository.existsByPrestacaoServicoIdAndPecaId(prestacaoServicoId, pecaId)).isFalse();
    }
}
