package br.com.fiap.soat.mecanica.application.servico;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.servico.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InativarServicoUseCase {

    private final ServicoRepository servicoRepository;

    public Servico executar(UUID id) {
        Servico servico = servicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

        servico.inativar();

        return servicoRepository.salvar(servico);
    }
}
