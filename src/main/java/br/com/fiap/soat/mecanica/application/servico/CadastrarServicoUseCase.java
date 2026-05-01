package br.com.fiap.soat.mecanica.application.servico;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.servico.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarServicoUseCase {

    private final ServicoRepository servicoRepository;

    public Servico executar(String nome, String descricao) {

        servicoRepository.buscarPorNome(nome)
                .ifPresent((servico) -> {
                    throw new RegraNegocioException("Serviço com esse nome já existe");
                });

        Servico servico = new Servico(nome, descricao);
        return servicoRepository.salvar(servico);
    }
}