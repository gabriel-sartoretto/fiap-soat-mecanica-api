package br.com.fiap.soat.mecanica.application.servico;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.servico.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarServicoUseCase {

    private final ServicoRepository servicoRepository;

    public Servico executar(UUID id, String nome, String descricao) {

        servicoRepository.buscarPorNome(nome)
                .filter(servicoEncontrada -> !servicoEncontrada.getId().equals(id))
                .ifPresent((servico) -> {
                    throw new RegraNegocioException("Serviço com esse nome já existe");
                });

        Servico servico = servicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não existe"));

        servico.alterar(nome, descricao);
        return servicoRepository.salvar(servico);
    }
}
