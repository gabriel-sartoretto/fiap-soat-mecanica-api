package br.com.fiap.soat.mecanica.application.veiculo;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;

    public void executar(String placa) {
        Veiculo veiculo = new Veiculo(placa);
        repository.salvar(veiculo);
    }
}
