package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarPecaPorIdUseCase {

    private final PecaRepository repository;

    public Peca executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peca nao encontrada"));
    }
}
