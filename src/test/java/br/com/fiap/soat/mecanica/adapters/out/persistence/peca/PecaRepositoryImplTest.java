package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.peca.mapper.PecaMapper;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({PecaRepositoryImpl.class, PecaMapper.class})
class PecaRepositoryImplTest {

    @Autowired
    private PecaRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar peça por ID")
    void deveSalvarEBuscarPorId() {
        // Arrange
        Peca peca = new Peca("Pastilha", "Bosch", new BigDecimal("150.00"), 10);

        // Act
        Peca salva = repository.salvar(peca);
        Optional<Peca> encontrada = repository.buscarPorId(salva.getId());

        // Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("Pastilha");
    }

    @Test
    @DisplayName("Deve buscar peça por nome")
    void deveBuscarPorNome() {
        // Arrange
        Peca peca = new Peca("Disco Freio", "Fremax", new BigDecimal("200.00"), 5);
        repository.salvar(peca);

        // Act
        Optional<Peca> encontrada = repository.buscarPorNome("Disco Freio");

        // Assert
        assertThat(encontrada).isPresent();
    }
}
