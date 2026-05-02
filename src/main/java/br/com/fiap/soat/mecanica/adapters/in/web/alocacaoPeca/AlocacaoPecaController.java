package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.mapper.AlocacaoPecaResponseMapper;
import br.com.fiap.soat.mecanica.application.alocacaoPeca.CadastrarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.application.alocacaoPeca.InativarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/alocacao-pecas")
@RequiredArgsConstructor
public class AlocacaoPecaController {

    private final CadastrarAlocacaoPecaUseCase cadastrarAlocacaoPecaUseCase;
    private final InativarAlocacaoPecaUseCase inativarAlocacaoPecaUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ALMOXARIFE')")
    @Operation(summary = "Cadastro de uma Alocação de Peças")
    public ResponseEntity<AlocacaoPecaResponse> cadastrar(@Valid @RequestBody AlocacaoPecaIncluirRequest request) {

        AlocacaoPeca alocacao = cadastrarAlocacaoPecaUseCase.executar(
                request.quantidadeNecessaria(),
                request.prestacaoServicoId(),
                request.pecaId()
        );

        return ResponseEntity.ok(AlocacaoPecaResponseMapper.toResponse(alocacao));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ALMOXARIFE')")
    @Operation(summary = "Inativação de uma Alocação de Peças")
    public ResponseEntity<AlocacaoPecaResponse> inativar(@PathVariable UUID id) {
        AlocacaoPeca alocacao = inativarAlocacaoPecaUseCase.executar(id);
        return ResponseEntity.ok(AlocacaoPecaResponseMapper.toResponse(alocacao));
    }
}