package br.com.fiap.soat.mecanica.application.veiculo.usecase;

import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;

    public Veiculo executar(String placa, String marca, String modelo, String ano, int quantidadeEixos, UUID clienteId) {
        Veiculo veiculo = new Veiculo(new Placa(placa), marca, modelo,
                ano, quantidadeEixos, clienteId);
        return repository.salvar(veiculo);
    }
}
