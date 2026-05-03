package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper.PrestacaoServicoMapper;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({PrestacaoServicoRepositoryImpl.class, PrestacaoServicoMapper.class})
class PrestacaoServicoRepositoryImplTest {

    @Autowired
    private PrestacaoServicoRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar prestação por ID")
    void deveSalvarEBuscarPorId() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();
        PrestacaoServico ps = new PrestacaoServico(new BigDecimal("200"), osId, servicoId);

        // Act
        PrestacaoServico salva = repository.salvar(ps);
        Optional<PrestacaoServico> encontrada = repository.buscarPorId(salva.getId());

        // Assert
        assertThat(encontrada).isPresent();
    }

    @Test
    @DisplayName("Deve buscar todas prestações por OS ID")
    void deveBuscarTodosPorOsId() {
        // Arrange
        UUID osId = UUID.randomUUID();
        repository.salvar(new PrestacaoServico(new BigDecimal("100"), osId, UUID.randomUUID()));
        repository.salvar(new PrestacaoServico(new BigDecimal("200"), osId, UUID.randomUUID()));

        // Act
        List<PrestacaoServico> lista = repository.buscarTodosPorOrdemServicoId(osId);

        // Assert
        assertThat(lista).hasSize(2);
    }
}
