package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.mapper.ClienteMapper;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
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
@Import({ClienteRepositoryImpl.class, ClienteMapper.class})
class ClienteRepositoryImplTest {

    @Autowired
    private ClienteRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar cliente por CPF")
    void deveSalvarEBuscarPorCpf() {
        // Arrange
        Cliente cliente = new Cliente("Cliente Teste", new CPF("52998224725"), null,
                new Email("teste@email.com"), null, UUID.randomUUID());

        // Act
        repository.salvar(cliente);
        Optional<Cliente> encontrado = repository.buscarPorCpf(new CPF("52998224725"));

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Cliente Teste");
    }

    @Test
    @DisplayName("Deve retornar vazio quando CPF não existe")
    void deveRetornarVazio_quandoCpfNaoExiste() {
        assertThat(repository.buscarPorCpf(new CPF("52998224725"))).isEmpty();
    }
}
