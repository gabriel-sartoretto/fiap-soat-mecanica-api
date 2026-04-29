package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper.PecaResponseMapper;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarPecaUseCase {

    private final PecaRepository repository;

    public Peca executar(PecaIncluirRequest request) {
        repository.buscarPorNome(request.nome())
                .ifPresent(peca -> {
                    throw new RegraNegocioException("Peca ja cadastrada");
                });

        Peca peca = PecaResponseMapper.toDomain(request);
        return repository.salvar(peca);
    }
}
