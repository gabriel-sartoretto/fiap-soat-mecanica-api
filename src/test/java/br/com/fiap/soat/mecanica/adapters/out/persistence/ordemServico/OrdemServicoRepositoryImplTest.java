package br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico.mapper.OrdemServicoMapper;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({OrdemServicoRepositoryImpl.class, OrdemServicoMapper.class})
class OrdemServicoRepositoryImplTest {

    @Autowired
    private OrdemServicoRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar OS por ID")
    void deveSalvarEBuscarPorId() {
        // Arrange
        UUID veiculoId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        OrdemServico os = new OrdemServico("Observação teste", veiculoId, usuarioId);

        // Act
        OrdemServico salva = repository.salvar(os);
        Optional<OrdemServico> encontrada = repository.buscarPorId(salva.getId());

        // Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getObservacao()).isEqualTo("Observação teste");
    }

    @Test
    @DisplayName("Deve buscar todas OS por veículo ID")
    void deveBuscarTodosPorVeiculoId() {
        // Arrange
        UUID veiculoId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        repository.salvar(new OrdemServico("OS 1", veiculoId, usuarioId));
        repository.salvar(new OrdemServico("OS 2", veiculoId, usuarioId));

        // Act
        List<OrdemServico> lista = repository.buscarTodosPorVeiculoId(veiculoId);

        // Assert
        assertThat(lista).hasSize(2);
    }
}
