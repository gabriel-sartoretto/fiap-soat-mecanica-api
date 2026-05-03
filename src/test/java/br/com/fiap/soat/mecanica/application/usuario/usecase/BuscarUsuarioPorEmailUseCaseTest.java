package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioPorEmailUseCaseTest {

    @Mock
    private UsuarioRepository repository;
    @InjectMocks
    private BuscarUsuarioPorEmailUseCase useCase;

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void deveBuscar_quandoEmailExiste() {
        // Arrange
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = useCase.executar("teste@email.com");

        // Assert
        assertThat(resultado).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecao_quandoNaoEncontrado() {
        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.executar("naoexiste@email.com"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
