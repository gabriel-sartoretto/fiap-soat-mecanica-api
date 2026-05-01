package br.com.fiap.soat.mecanica.application.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarTodosPrestacaoServicoProOrdemServicoIdUseCase {

    private final PrestacaoServicoRepository prestacaoServicoRepository;

    public List<PrestacaoServico> executar(UUID ordemServicoId) {

        return prestacaoServicoRepository.buscarTodosPorOrdemServicoId(ordemServicoId);
    }
}