package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario;

import br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.mapper.UsuarioMapper;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
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
@Import({UsuarioRepositoryImpl.class, UsuarioMapper.class})
class UsuarioRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private UsuarioRepositoryImpl repository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Mecanico Teste", "Senha@123", new Email("mecanico@test.com"), CargoEnum.MECANICO);
    }

    @Test
    @DisplayName("Deve salvar e buscar usuario por email")
    void deveSalvarEBuscarPorEmail() {
        Usuario salvo = repository.salvar(usuario);
        Optional<Usuario> encontrado = repository.buscarPorEmail("mecanico@test.com");

        assertThat(salvo.getId()).isNotNull();
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Mecanico Teste");
    }

    @Test
    @DisplayName("Deve retornar vazio quando email nao existe")
    void deveRetornarVazio_quandoEmailNaoExiste() {
        Optional<Usuario> encontrado = repository.buscarPorEmail("inexistente@test.com");

        assertThat(encontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar usuario por ID")
    void deveBuscarPorId() {
        Usuario salvo = repository.salvar(usuario);

        Optional<Usuario> encontrado = repository.buscarPorId(salvo.getId());

        assertThat(encontrado).isPresent();
    }
}
