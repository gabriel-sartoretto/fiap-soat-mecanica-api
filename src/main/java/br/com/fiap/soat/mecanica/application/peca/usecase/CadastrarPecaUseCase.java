package br.com.fiap.soat.mecanica.application.peca.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CadastrarPecaUseCase {

    private final PecaRepository repository;

    public Peca executar(String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        repository.buscarPorNome(nome)
                .ifPresent(peca -> {
                    throw new RegraNegocioException("Peça já cadastrada");
                });

        Peca peca = new Peca(nome, marca, valorUnitario, quantidadeEstoque);
        return repository.salvar(peca);
    }
}
