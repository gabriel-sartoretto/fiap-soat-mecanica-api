package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario;

import br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.mapper.UsuarioMapper;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import org.junit.jupiter.api.BeforeEach;
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
@Import({UsuarioRepositoryImpl.class, UsuarioMapper.class})
class UsuarioRepositoryImplTest {

    @Autowired
    private UsuarioRepositoryImpl repository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Mecânico Teste", "Senha@123", new Email("mecanico@test.com"), CargoEnum.MECANICO);
    }

    @Test
    @DisplayName("Deve salvar e buscar usuário por email")
    void deveSalvarEBuscarPorEmail() {
        // Act
        Usuario salvo = repository.salvar(usuario);
        Optional<Usuario> encontrado = repository.buscarPorEmail("mecanico@test.com");

        // Assert
        assertThat(salvo.getId()).isNotNull();
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Mecânico Teste");
    }

    @Test
    @DisplayName("Deve retornar vazio quando email não existe")
    void deveRetornarVazio_quandoEmailNaoExiste() {
        // Act
        Optional<Usuario> encontrado = repository.buscarPorEmail("inexistente@test.com");

        // Assert
        assertThat(encontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarPorId() {
        // Arrange
        Usuario salvo = repository.salvar(usuario);

        // Act
        Optional<Usuario> encontrado = repository.buscarPorId(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
    }
}
