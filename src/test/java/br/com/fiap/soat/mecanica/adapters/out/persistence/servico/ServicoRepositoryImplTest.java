package br.com.fiap.soat.mecanica.adapters.out.persistence.servico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.servico.mapper.ServicoMapper;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({ServicoRepositoryImpl.class, ServicoMapper.class})
class ServicoRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private ServicoRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar servico por ID")
    void deveSalvarEBuscarPorId() {
        Servico servico = new Servico("Troca de Oleo", "Desc");

        Servico salvo = repository.salvar(servico);
        Optional<Servico> encontrado = repository.buscarPorId(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Troca de Oleo");
    }

    @Test
    @DisplayName("Deve buscar servico por nome")
    void deveBuscarPorNome() {
        Servico servico = new Servico("Balanceamento", "Desc");
        repository.salvar(servico);

        Optional<Servico> encontrado = repository.buscarPorNome("Balanceamento");

        assertThat(encontrado).isPresent();
    }
}
