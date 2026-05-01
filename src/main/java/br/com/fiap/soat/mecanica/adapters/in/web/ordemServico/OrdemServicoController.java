package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.OrdemServicoIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.OrdemServicoResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper.OrdemServicoResponseMapper;
import br.com.fiap.soat.mecanica.application.ordemServico.usecase.BuscarOrdemServicoPorIdUseCase;
import br.com.fiap.soat.mecanica.application.ordemServico.usecase.BuscarTodosOrdemServicoPorVeiculoIdUseCase;
import br.com.fiap.soat.mecanica.application.ordemServico.usecase.CadastrarOrdemServicoUseCase;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("ordem-servicos")
public class OrdemServicoController {

    private final CadastrarOrdemServicoUseCase cadastrarOrdemServicoUseCase;
    private final BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;
    private final BuscarTodosOrdemServicoPorVeiculoIdUseCase buscarTodosOrdemServicoPorVeiculoIdUseCase;

    @PostMapping
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Cadastrar uma Ordem de Serviço")
    public ResponseEntity<OrdemServicoResponse> cadastrar(@Valid @RequestBody OrdemServicoIncluirRequest request) {

        OrdemServico os = cadastrarOrdemServicoUseCase.executar(request.observacao(), request.veiculoId());

        return ResponseEntity.ok(OrdemServicoResponseMapper.toResponse(os));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Buscar uma Ordem de Serviço por ID")
    public ResponseEntity<OrdemServicoResponse> buscarPorId(@PathVariable UUID id) {

        OrdemServico os = buscarOrdemServicoPorIdUseCase.executar(id);

        return ResponseEntity.ok(OrdemServicoResponseMapper.toResponse(os));
    }

    @GetMapping("/veiculo/{veiculoId}")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Buscar Ordem de serviços por veículo ID")
    public ResponseEntity<List<OrdemServicoResponse>> buscarTodosPorVeiculoId(
            @PathVariable UUID veiculoId) {

        List<OrdemServico> ordemServicos = buscarTodosOrdemServicoPorVeiculoIdUseCase.executar(veiculoId);

        List<OrdemServicoResponse> response = ordemServicos.stream()
                .map(OrdemServicoResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}