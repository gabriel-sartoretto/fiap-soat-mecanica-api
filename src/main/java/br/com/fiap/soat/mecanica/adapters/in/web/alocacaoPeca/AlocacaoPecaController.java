package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.mapper.AlocacaoPecaResponseMapper;
import br.com.fiap.soat.mecanica.application.alocacaoPeca.CadastrarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alocacao-pecas")
@RequiredArgsConstructor
public class AlocacaoPecaController {

    private final CadastrarAlocacaoPecaUseCase cadastrarAlocacaoPecaUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ALMOXARIFE')")
    public ResponseEntity<AlocacaoPecaResponse> incluir(@Valid @RequestBody AlocacaoPecaIncluirRequest request) {

        AlocacaoPeca alocacao = cadastrarAlocacaoPecaUseCase.executar(
                request.quantidadeNecessaria(),
                request.prestacaoServicoId(),
                request.pecaId()
        );

        return ResponseEntity.ok(AlocacaoPecaResponseMapper.toResponse(alocacao));
    }
}