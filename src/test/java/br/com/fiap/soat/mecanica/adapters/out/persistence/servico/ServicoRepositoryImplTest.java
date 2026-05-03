package br.com.fiap.soat.mecanica.adapters.out.persistence.servico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.servico.mapper.ServicoMapper;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({ServicoRepositoryImpl.class, ServicoMapper.class})
class ServicoRepositoryImplTest {

    @Autowired
    private ServicoRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar serviço por ID")
    void deveSalvarEBuscarPorId() {
        // Arrange
        Servico servico = new Servico("Troca de Óleo", "Desc");

        // Act
        Servico salvo = repository.salvar(servico);
        Optional<Servico> encontrado = repository.buscarPorId(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Troca de Óleo");
    }

    @Test
    @DisplayName("Deve buscar serviço por nome")
    void deveBuscarPorNome() {
        // Arrange
        Servico servico = new Servico("Alinhamento", "Desc");
        repository.salvar(servico);

        // Act
        Optional<Servico> encontrado = repository.buscarPorNome("Alinhamento");

        // Assert
        assertThat(encontrado).isPresent();
    }
}
