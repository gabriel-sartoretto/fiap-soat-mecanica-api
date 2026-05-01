package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto.PrestacaoServicoIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto.PrestacaoServicoResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.mapper.PrestacaoServicoResponseMapper;
import br.com.fiap.soat.mecanica.application.prestacaoServico.*;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prestacao-servico")
@RequiredArgsConstructor
public class PrestacaoServicoController {

    private final CadastrarPrestacaoServicoUseCase cadastrarUseCase;
    private final BuscarTodosPrestacaoServicoProOrdemServicoIdUseCase buscarTodosPrestacaoServicoProOrdemServicoIdUseCase;
    private final AtivarPrestacaoServicoUseCase ativarPrestacaoServicoUseCase;
    private final InativarPrestacaoServicoUseCase inativarPrestacaoServicoUseCase;
    private final FinalizarPrestacaoServicoUseCase finalizarPrestacaoServicoUseCase;

    @PostMapping
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Cadastrar Prestação de Serviço")
    public ResponseEntity<PrestacaoServicoResponse> cadastrar(@Valid @RequestBody PrestacaoServicoIncluirRequest request) {

        PrestacaoServico ps = cadastrarUseCase.executar(
                request.precoMaoDeObra(),
                request.ordemServicoId(),
                request.servicoId()
        );

        return ResponseEntity.ok(PrestacaoServicoResponseMapper.toResponse(ps));
    }

    @GetMapping("por-ordem-servico/{ordemServicoId}")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Buscar todos Prestação de Serviços por Ordem Serviço ID")
    public ResponseEntity<List<PrestacaoServicoResponse>> buscarTodosPorOrdemServicoId(@PathVariable UUID ordemServicoId) {

        List<PrestacaoServico> prestacaoServicos = buscarTodosPrestacaoServicoProOrdemServicoIdUseCase.executar(ordemServicoId);

        List<PrestacaoServicoResponse> response = prestacaoServicos.stream()
                .map(PrestacaoServicoResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Ativar Prestação de Serviço por Id")
    public ResponseEntity<PrestacaoServicoResponse> ativar(@PathVariable UUID id) {

        PrestacaoServico ps = ativarPrestacaoServicoUseCase.executar(id);

        return ResponseEntity.ok(PrestacaoServicoResponseMapper.toResponse(ps));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Inativar Prestação de Serviço por Id")
    public ResponseEntity<PrestacaoServicoResponse> inativar(@PathVariable UUID id) {

        PrestacaoServico ps = inativarPrestacaoServicoUseCase.executar(id);

        return ResponseEntity.ok(PrestacaoServicoResponseMapper.toResponse(ps));
    }

    @PatchMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('MECANICO')")
    public ResponseEntity<PrestacaoServicoResponse> finalizar(@PathVariable UUID id) {

        PrestacaoServico ps = finalizarPrestacaoServicoUseCase.executar(id);

        return ResponseEntity.ok(PrestacaoServicoResponseMapper.toResponse(ps));
    }
}