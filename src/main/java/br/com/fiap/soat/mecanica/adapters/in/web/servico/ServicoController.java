package br.com.fiap.soat.mecanica.adapters.in.web.servico;

import br.com.fiap.soat.mecanica.adapters.in.web.servico.dto.ServicoIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.servico.dto.ServicoResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.servico.mapper.ServicoResponseMapper;
import br.com.fiap.soat.mecanica.application.servico.AlterarServicoUseCase;
import br.com.fiap.soat.mecanica.application.servico.AtivarServicoUseCase;
import br.com.fiap.soat.mecanica.application.servico.CadastrarServicoUseCase;
import br.com.fiap.soat.mecanica.application.servico.InativarServicoUseCase;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final CadastrarServicoUseCase cadastrarUseCase;
    private final AlterarServicoUseCase alterarServicoUseCase;
    private final AtivarServicoUseCase ativarServicoUseCase;
    private final InativarServicoUseCase inativarServicoUseCase;

    @PostMapping
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Cadastrar um serviço")
    public ResponseEntity<ServicoResponse> cadastrar(@Valid @RequestBody ServicoIncluirRequest request) {

        Servico servico = cadastrarUseCase.executar(
                request.nome(),
                request.descricao()
        );

        return ResponseEntity.ok(ServicoResponseMapper.toResponse(servico));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Alterar um serviço")
    public ResponseEntity<ServicoResponse> alterar(
            @PathVariable UUID id,
            @Valid @RequestBody ServicoIncluirRequest request) {

        Servico servico = alterarServicoUseCase.executar(
                id,
                request.nome(),
                request.descricao()
        );

        return ResponseEntity.ok(ServicoResponseMapper.toResponse(servico));
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Ativar um serviço")
    public ResponseEntity<ServicoResponse> ativar(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ServicoResponseMapper.toResponse(ativarServicoUseCase.executar(id))
        );
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('MECANICO')")
    @Operation(summary = "Inativar um serviço")
    public ResponseEntity<ServicoResponse> inativar(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ServicoResponseMapper.toResponse(inativarServicoUseCase.executar(id))
        );
    }
}
