package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.mapper.VeiculoMapper;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
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
@Import({VeiculoRepositoryImpl.class, VeiculoMapper.class})
class VeiculoRepositoryImplTest {

    @Autowired
    private VeiculoRepositoryImpl repository;

    @Test
    @DisplayName("Deve salvar e buscar veículo por placa")
    void deveSalvarEBuscarPorPlaca() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, clienteId);

        // Act
        repository.salvar(veiculo);
        Optional<Veiculo> encontrado = repository.buscarPorPlaca("ABC1234");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getMarca()).isEqualTo("Toyota");
    }

    @Test
    @DisplayName("Deve buscar todos veículos por cliente ID")
    void deveBuscarTodosPorClienteId() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        Veiculo v1 = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", "2023", 2, clienteId);
        Veiculo v2 = new Veiculo(new Placa("DEF5G67"), "Honda", "Civic", "2022", 2, clienteId);
        repository.salvar(v1);
        repository.salvar(v2);

        // Act
        List<Veiculo> veiculos = repository.buscarTodosPorClienteId(clienteId);

        // Assert
        assertThat(veiculos).hasSize(2);
    }
}
