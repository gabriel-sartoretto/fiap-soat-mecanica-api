package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarOrdemServicoUseCase {

    private final OrdemServicoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public OrdemServico executar(String observacao, UUID veiculoId, UUID usuarioId) {

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (usuario.getCargoEnum() != CargoEnum.MECANICO) {
            throw new RegraNegocioException("Somente mecânicos podem criar OS");
        }

        OrdemServico os = new OrdemServico(observacao, veiculoId, usuarioId);
        return repository.salvar(os);
    }
}
