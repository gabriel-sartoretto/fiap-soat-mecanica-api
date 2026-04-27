package br.com.fiap.soat.mecanica.application.veiculo;

import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto.VeiculoIncluirRequest;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;

    public Veiculo executar(VeiculoIncluirRequest request) {
        Veiculo veiculo = new Veiculo(request.placa(), request.marca(), request.modelo(),
                request.ano(), request.quantidadeEixos());
        return repository.salvar(veiculo);
    }
}
