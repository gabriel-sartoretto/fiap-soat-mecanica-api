package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.peca.mapper.PecaMapper;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({PecaRepositoryImpl.class, PecaMapper.class})
class PecaRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private PecaRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar peça por ID")
    void deveSalvarEBuscarPorId() {
        Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

        Peca salva = repository.salvar(peca);
        Optional<Peca> encontrada = repository.buscarPorId(salva.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("Pastilha");
    }

    @Test
    @DisplayName("Deve buscar peça por nome")
    void deveBuscarPorNome() {
        Peca peca = new Peca("Disco Freio", "Fremax", new BigDecimal("200.00"), 5);
        repository.salvar(peca);

        Optional<Peca> encontrada = repository.buscarPorNome("Disco Freio");

        assertThat(encontrada).isPresent();
    }
}
