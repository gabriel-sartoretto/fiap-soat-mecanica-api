package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @InjectMocks
    private CadastrarUsuarioUseCase useCase;

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrar_quandoDadosValidos() {
        // Arrange
        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode(anyString())).thenReturn("$2a$10$hash");
        Usuario usuario = TestDataFactory.criarUsuarioMecanico();
        when(repository.salvar(any())).thenReturn(usuario);

        // Act
        Usuario resultado = useCase.executar("Senha@123", "Teste", "novo@email.com", CargoEnum.MECANICO);

        // Assert
        assertThat(resultado).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já cadastrado")
    void deveLancarExcecao_quandoEmailDuplicado() {
        // Arrange
        Usuario existente = TestDataFactory.criarUsuarioMecanico();
        when(repository.buscarPorEmail(anyString())).thenReturn(Optional.of(existente));

        // Act & Assert
        assertThatThrownBy(() -> useCase.executar("Senha@123", "Teste", "teste@email.com", CargoEnum.MECANICO))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Usuário já cadastrada");
    }
}
