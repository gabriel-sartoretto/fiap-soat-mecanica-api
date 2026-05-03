package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto;

import br.com.fiap.soat.mecanica.domain.enums.SituacaoOrdemServicoEnum;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrdemServicoResponse(
        UUID id,
        StatusRecursoEnum status,
        SituacaoOrdemServicoEnum situacao,
        LocalDateTime dataRecebida,
        LocalDateTime dataDiagnostico,
        LocalDateTime dataAguardandoAprovacao,
        LocalDateTime dataExecucao,
        LocalDateTime dataFinalizada,
        LocalDateTime dataEntregue,
        Boolean pago,
        BigDecimal valorTotal,
        String observacao,
        UUID veiculoId,
        UUID usuarioId
) {
}
