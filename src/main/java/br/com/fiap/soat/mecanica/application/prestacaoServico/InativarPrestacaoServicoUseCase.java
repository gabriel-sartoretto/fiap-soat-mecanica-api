package br.com.fiap.soat.mecanica.application.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InativarPrestacaoServicoUseCase {

    private final PrestacaoServicoRepository repository;

    public PrestacaoServico executar(UUID id) {

        PrestacaoServico ps = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestação de serviço não encontrada"));

        ps.inativar();

        return repository.salvar(ps);
    }
}
