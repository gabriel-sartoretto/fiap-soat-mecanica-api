package br.com.fiap.soat.mecanica.adapters.in.web.peca;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaAtualizarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper.PecaResponseMapper;
import br.com.fiap.soat.mecanica.application.peca.usecase.AlterarPecaUseCase;
import br.com.fiap.soat.mecanica.application.peca.usecase.BuscarPecaPorIdUseCase;
import br.com.fiap.soat.mecanica.application.peca.usecase.CadastrarPecaUseCase;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final CadastrarPecaUseCase cadastrarPecaUseCase;
    private final AlterarPecaUseCase alterarPecaUseCase;
    private final BuscarPecaPorIdUseCase buscarPecaPorIdUseCase;

    @PostMapping
    @Operation(summary = "Cadastrar uma peca")
    @PreAuthorize("hasRole('ALMOXARIFADO')")
    public ResponseEntity<PecaResponse> cadastrar(@Valid @RequestBody PecaIncluirRequest request) {
        Peca peca = cadastrarPecaUseCase.executar(
                request.nome(),
                request.marca(),
                request.valorUnitario(),
                request.quantidadeEstoque()
        );
        return ResponseEntity.ok(PecaResponseMapper.toResponse(peca));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma peca")
    @PreAuthorize("hasRole('ALMOXARIFADO')")
    public ResponseEntity<PecaResponse> alterar(@PathVariable UUID id,
                                                @Valid @RequestBody PecaAtualizarRequest request) {
        Peca peca = alterarPecaUseCase.executar(
                id,
                request.nome(),
                request.marca(),
                request.valorUnitario(),
                request.quantidadeEstoque()
        );
        return ResponseEntity.ok(PecaResponseMapper.toResponse(peca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma peca por ID")
    @PreAuthorize("hasRole('ALMOXARIFADO')")
    public ResponseEntity<PecaResponse> buscarPorId(@PathVariable UUID id) {
        Peca peca = buscarPecaPorIdUseCase.executar(id);
        return ResponseEntity.ok(PecaResponseMapper.toResponse(peca));
    }
}
