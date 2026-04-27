package br.com.fiap.soat.mecanica.adapters.in.web.veiculo;

import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto.VeiculoIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto.VeiculoResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.mapper.VeiculoResponseMapper;
import br.com.fiap.soat.mecanica.application.veiculo.BuscarVeiculoPorIdUseCase;
import br.com.fiap.soat.mecanica.application.veiculo.CadastrarVeiculoUseCase;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;

    @PostMapping
    @Operation(summary = "Cadastrar um veículo")
    //@PreAuthorize("")
    public ResponseEntity<VeiculoResponse> cadastrar(@Valid @RequestBody VeiculoIncluirRequest request) {
        Veiculo veiculo = cadastrarVeiculoUseCase.executar(request);
        return ResponseEntity.ok(VeiculoResponseMapper.toResponse(veiculo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um veículo por ID")
    //@PreAuthorize("")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable("id") UUID id) {
        Veiculo veiculo = buscarVeiculoPorIdUseCase.executar(id);
        return ResponseEntity.ok(VeiculoResponseMapper.toResponse(veiculo));
    }
}
