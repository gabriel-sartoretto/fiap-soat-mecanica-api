package br.com.fiap.soat.mecanica.interfaces.veiculo;

import br.com.fiap.soat.mecanica.application.veiculo.CadastrarVeiculoUseCase;
import br.com.fiap.soat.mecanica.interfaces.veiculo.dto.VeiculoIncluirRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;

    @PostMapping
    @Operation(summary = "Cadastrar um veículo")
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody VeiculoIncluirRequest request) {
        cadastrarVeiculoUseCase.executar(request.placa());
        return ResponseEntity.ok().build();
    }
}
