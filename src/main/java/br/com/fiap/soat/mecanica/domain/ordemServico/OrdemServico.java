package br.com.fiap.soat.mecanica.domain.ordemServico;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.SituacaoOrdemServico;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdemServico extends Principal {

    private UUID id;

    private SituacaoOrdemServico situacao;

    private LocalDateTime dataRecebida;
    private LocalDateTime dataDiagnostico;
    private LocalDateTime dataExecucao;
    private LocalDateTime dataFinalizada;
    private LocalDateTime dataEntregue;

    private Boolean pago;
    private BigDecimal valorTotal;
    private String observacao;

    private UUID veiculoId;
    private UUID usuarioId;

    public OrdemServico(String observacao, UUID veiculoId, UUID usuarioId) {
        validar(veiculoId, usuarioId);

        this.observacao = observacao;
        this.veiculoId = veiculoId;
        this.usuarioId = usuarioId;

        this.situacao = SituacaoOrdemServico.RECEBIDA;
        this.dataRecebida = LocalDateTime.now();
    }

    public static OrdemServico reconstruir(
            UUID id,
            StatusRecursoEnum status,
            SituacaoOrdemServico situacao,
            LocalDateTime dataRecebida,
            LocalDateTime dataDiagnostico,
            LocalDateTime dataExecucao,
            LocalDateTime dataFinalizada,
            LocalDateTime dataEntregue,
            Boolean pago,
            BigDecimal valorTotal,
            String observacao,
            UUID veiculoId,
            UUID usuarioId
    ) {
        OrdemServico os = new OrdemServico();

        os.id = id;
        os.status = status;
        os.situacao = situacao;
        os.dataRecebida = dataRecebida;
        os.dataDiagnostico = dataDiagnostico;
        os.dataExecucao = dataExecucao;
        os.dataFinalizada = dataFinalizada;
        os.dataEntregue = dataEntregue;
        os.pago = pago;
        os.valorTotal = valorTotal;
        os.observacao = observacao;
        os.veiculoId = veiculoId;
        os.usuarioId = usuarioId;

        return os;
    }

    public void iniciarDiagnostico() {
        validarTransicao(SituacaoOrdemServico.RECEBIDA);

        this.situacao = SituacaoOrdemServico.EM_DIAGNOSTICO;
        this.dataDiagnostico = LocalDateTime.now();
    }

    public void iniciarExecucao(BigDecimal valorTotal) {
        validarTransicao(SituacaoOrdemServico.EM_DIAGNOSTICO);

        if (valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor inválido");
        }

        this.situacao = SituacaoOrdemServico.EM_EXECUCAO;
        this.dataExecucao = LocalDateTime.now();
        this.valorTotal = valorTotal;
        this.pago = false;
    }

    public void finalizar() {
        validarTransicao(SituacaoOrdemServico.EM_EXECUCAO);

        this.situacao = SituacaoOrdemServico.FINALIZADA;
        this.dataFinalizada = LocalDateTime.now();
    }

    public void entregar(Boolean pago) {
        validarTransicao(SituacaoOrdemServico.FINALIZADA);

        if (!pago) throw new RegraNegocioException("É necessário pagar antes de entregar o veículo");

        this.situacao = SituacaoOrdemServico.ENTREGUE;
        this.dataEntregue = LocalDateTime.now();
    }

    private void validarTransicao(SituacaoOrdemServico esperado) {
        if (this.situacao != esperado) {
            throw new RegraNegocioException("Transição inválida");
        }
    }

    private void validar(UUID veiculoId, UUID usuarioId) {
        if (veiculoId == null) throw new RegraNegocioException("O veículo obrigatório");

        if (usuarioId == null) throw new RegraNegocioException("O mecânico obrigatório");
    }
}
