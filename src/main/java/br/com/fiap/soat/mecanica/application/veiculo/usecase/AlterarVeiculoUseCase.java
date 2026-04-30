package br.com.fiap.soat.mecanica.application.veiculo.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;

    public Veiculo executar(UUID id, String marca, String modelo, String ano, Integer quantidadeEixos) {

        Veiculo veiculo = veiculoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));

        veiculo.alterar(marca, modelo, ano, quantidadeEixos);
        return veiculoRepository.salvar(veiculo);
    }
}
