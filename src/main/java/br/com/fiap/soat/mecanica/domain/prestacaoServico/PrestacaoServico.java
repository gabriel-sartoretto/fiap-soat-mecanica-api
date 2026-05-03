package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.OperacaoSubtotalEnum;
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
public class PrestacaoServico extends Principal {

    private UUID id;
    private BigDecimal precoMaoDeObra;
    private BigDecimal subtotal;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private UUID ordemServicoId;
    private UUID servicoId;

    public PrestacaoServico(BigDecimal precoMaoDeObra,
                            UUID ordemServicoId,
                            UUID servicoId) {

        validar(precoMaoDeObra, ordemServicoId, servicoId);

        this.precoMaoDeObra = precoMaoDeObra;
        this.ordemServicoId = ordemServicoId;
        this.servicoId = servicoId;
        this.subtotal = calcularSubtotal(precoMaoDeObra, OperacaoSubtotalEnum.SOMAR);
    }

    public static PrestacaoServico reconstruir(
            UUID id,
            StatusRecursoEnum status,
            BigDecimal precoMaoDeObra,
            BigDecimal subtotal,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            UUID ordemServicoId,
            UUID servicoId
    ) {
        PrestacaoServico prestacaoServico = new PrestacaoServico();
        prestacaoServico.id = id;
        prestacaoServico.status = status;
        prestacaoServico.precoMaoDeObra = precoMaoDeObra;
        prestacaoServico.subtotal = subtotal;
        prestacaoServico.dataInicio = dataInicio;
        prestacaoServico.dataFim = dataFim;
        prestacaoServico.ordemServicoId = ordemServicoId;
        prestacaoServico.servicoId = servicoId;

        return prestacaoServico;
    }

    public void iniciarServico(boolean ordemServicoEmExecucao) {
        if (!ordemServicoEmExecucao) throw new
                RegraNegocioException("Não é possível inicar um serviço se a Ordem de Serviço não está em execução");
        if (this.isInativo())
            throw new RegraNegocioException("Não é possível iniciar um serviço inativo");

        if (this.dataInicio != null)
            throw new RegraNegocioException("Serviço já iniciado");

        this.dataInicio = LocalDateTime.now();
    }

    public void finalizarServico() {

        if (this.isInativo())
            throw new RegraNegocioException("Não é possível finalizar um serviço inativo");

        if (this.dataInicio == null)
            throw new RegraNegocioException("Serviço ainda não foi iniciado");

        if (this.dataFim != null)
            throw new RegraNegocioException("Serviço já finalizado");

        this.dataFim = LocalDateTime.now();
    }

    private BigDecimal calcularSubtotal(BigDecimal valor, OperacaoSubtotalEnum operacao) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0)
            throw new RegraNegocioException("Valor para subtotal inválido");

        if (this.subtotal == null) {
            this.subtotal = BigDecimal.ZERO;
        }

        return switch (operacao) {
            case SOMAR -> this.subtotal.add(valor);
            case SUBTRAIR -> {
                BigDecimal novoSubtotal = this.subtotal.subtract(valor);

                if (novoSubtotal.compareTo(this.precoMaoDeObra) < 0) {
                    yield this.precoMaoDeObra;
                }

                yield novoSubtotal;
            }
        };
    }

    public boolean isFinalizada() {
        return this.dataFim != null;
    }

    public void validarPodeAlterar() {
        if (isInativo())
            throw new RegraNegocioException("Prestação de serviço está inativa");

        if (isFinalizada())
            throw new RegraNegocioException("Prestação de serviço já finalizada");
    }

    private void validar(BigDecimal precoMaoDeObra,
                         UUID ordemServicoId,
                         UUID servicoId) {

        if (precoMaoDeObra == null || precoMaoDeObra.compareTo(BigDecimal.ZERO) <= 0)
            throw new RegraNegocioException("Preço de mão de obra é obrigatório");

        if (ordemServicoId == null)
            throw new RegraNegocioException("Ordem de serviço obrigatória");

        if (servicoId == null)
            throw new RegraNegocioException("Serviço obrigatório");
    }

    public BigDecimal adicionarValorPeca(BigDecimal valorUnitario, Integer quantidade) {
        BigDecimal valorTotalPeca = calcularValorTotalPeca(valorUnitario, quantidade);

        this.subtotal = calcularSubtotal(valorTotalPeca, OperacaoSubtotalEnum.SOMAR);
        return valorTotalPeca;
    }

    public BigDecimal removerValorPeca(BigDecimal valorUnitario, Integer quantidade) {
        BigDecimal valorTotalPeca = calcularValorTotalPeca(valorUnitario, quantidade);

        BigDecimal subtotalAntes = this.subtotal == null ? BigDecimal.ZERO : this.subtotal;

        this.subtotal = calcularSubtotal(valorTotalPeca, OperacaoSubtotalEnum.SUBTRAIR);

        BigDecimal valorRemovido = subtotalAntes.subtract(this.subtotal);

        if (valorRemovido.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return valorRemovido;
    }

    private BigDecimal calcularValorTotalPeca(BigDecimal valorUnitario, Integer quantidade) {
        if (valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Valor unitário da peça inválido");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException("Quantidade da peça inválida");
        }

        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
