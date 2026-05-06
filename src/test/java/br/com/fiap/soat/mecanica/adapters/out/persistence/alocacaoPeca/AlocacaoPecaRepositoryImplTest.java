package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.mapper.AlocacaoPecaMapper;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({AlocacaoPecaRepositoryImpl.class, AlocacaoPecaMapper.class})
class AlocacaoPecaRepositoryImplTest {

    @Autowired
    private AlocacaoPecaRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar alocação por ID")
    void deveSalvarEBuscarPorId() {
        // Arrange
        AlocacaoPeca alocacao = new AlocacaoPeca(2, UUID.randomUUID(), UUID.randomUUID());

        // Act
        AlocacaoPeca salva = repository.salvar(alocacao);
        Optional<AlocacaoPeca> encontrada = repository.buscarPorId(salva.getId());

        // Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getQuantidadeNecessaria()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve verificar alocacao ativa por prestacao e peca")
    void deveVerificarExistenciaPorPrestacaoServicoEPeca() {
        UUID prestacaoServicoId = UUID.randomUUID();
        UUID pecaId = UUID.randomUUID();
        repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, pecaId));

        assertThat(repository.existsByPrestacaoServicoIdAndPecaId(prestacaoServicoId, pecaId)).isTrue();
    }

    @Test
    @DisplayName("Deve buscar todas alocacoes por prestacao de servico")
    void deveBuscarTodosPorPrestacaoServicoId() {
        UUID prestacaoServicoId = UUID.randomUUID();
        repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, UUID.randomUUID()));
        repository.salvar(new AlocacaoPeca(3, prestacaoServicoId, UUID.randomUUID()));

        assertThat(repository.buscarTodosPorPrestacaoServicoId(prestacaoServicoId)).hasSize(2);
    }

    @Test
    @DisplayName("Nao deve considerar alocacao inativa como existente")
    void naoDeveConsiderarAlocacaoInativaComoExistente() {
        UUID prestacaoServicoId = UUID.randomUUID();
        UUID pecaId = UUID.randomUUID();
        AlocacaoPeca inativa = repository.salvar(new AlocacaoPeca(2, prestacaoServicoId, pecaId));
        inativa.inativar();
        repository.salvar(inativa);

        assertThat(repository.existsByPrestacaoServicoIdAndPecaId(prestacaoServicoId, pecaId)).isFalse();
    }
}
