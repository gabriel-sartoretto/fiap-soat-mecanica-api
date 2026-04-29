package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaAtualizarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper.PecaResponseMapper;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtualizarPecaUseCase {

    private final PecaRepository repository;
    private final BuscarPecaPorIdUseCase buscarPecaPorIdUseCase;

    public Peca executar(UUID id, PecaAtualizarRequest request) {
        Peca peca = buscarPecaPorIdUseCase.executar(id);

        repository.buscarPorNome(request.nome())
                .filter(pecaEncontrada -> !pecaEncontrada.getId().equals(id))
                .ifPresent(pecaEncontrada -> {
                    throw new RegraNegocioException("Peca ja cadastrada");
                });

        PecaResponseMapper.atualizarDomain(peca, request);
        return repository.salvar(peca);
    }
}
