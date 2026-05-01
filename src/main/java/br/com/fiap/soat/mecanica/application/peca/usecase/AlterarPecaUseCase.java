package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarPecaUseCase {

    private final PecaRepository repository;
    private final BuscarPecaPorIdUseCase buscarPecaPorIdUseCase;

    public Peca executar(UUID id, String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        Peca peca = buscarPecaPorIdUseCase.executar(id);

        repository.buscarPorNome(nome)
                .filter(pecaEncontrada -> !pecaEncontrada.getId().equals(id))
                .ifPresent(pecaEncontrada -> {
                    throw new RegraNegocioException("Peça já cadastrada");
                });

        peca.alterar(nome, marca, valorUnitario, quantidadeEstoque);
        return repository.salvar(peca);
    }
}
