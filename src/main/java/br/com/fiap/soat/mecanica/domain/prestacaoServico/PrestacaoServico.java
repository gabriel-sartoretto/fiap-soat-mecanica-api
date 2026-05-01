package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.Principal;
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
    private Integer quantidadeNecessaria;
    private BigDecimal precoMaoDeObra;
    private BigDecimal subtotal;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private UUID ordemServicoId;
    private UUID servicoId;

    public PrestacaoServico(Integer quantidadeNecessaria,
                            BigDecimal precoMaoDeObra,
                            UUID ordemServicoId,
                            UUID servicoId) {

        validar(quantidadeNecessaria, precoMaoDeObra, ordemServicoId, servicoId);

        this.quantidadeNecessaria = quantidadeNecessaria;
        this.precoMaoDeObra = precoMaoDeObra;
        this.ordemServicoId = ordemServicoId;
        this.servicoId = servicoId;
        this.subtotal = calcularSubtotal();
    }

    public static PrestacaoServico reconstruir(
            UUID id,
            StatusRecursoEnum status,
            Integer quantidadeNecessaria,
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
        prestacaoServico.quantidadeNecessaria = quantidadeNecessaria;
        prestacaoServico.precoMaoDeObra = precoMaoDeObra;
        prestacaoServico.subtotal = subtotal;
        prestacaoServico.dataInicio = dataInicio;
        prestacaoServico.dataFim = dataFim;
        prestacaoServico.ordemServicoId = ordemServicoId;
        prestacaoServico.servicoId = servicoId;

        return prestacaoServico;
    }

    // todo quando a os mudar para em andamento ele chama aqui
    public void iniciarServico() {
        if (this.isInativo()) {
            throw new RegraNegocioException("Não é possível iniciar um serviço inativo");
        }

        if (this.dataInicio != null) {
            throw new RegraNegocioException("Serviço já iniciado");
        }

        this.dataInicio = LocalDateTime.now();
    }

    public void finalizarServico() {

        if (this.isInativo()) {
            throw new RegraNegocioException("Não é possível finalizar um serviço inativo");
        }

        if (this.dataFim != null) {
            throw new RegraNegocioException("Serviço já finalizado");
        }

        this.dataFim = LocalDateTime.now();
    }

    private BigDecimal calcularSubtotal() {
        // TODO tem q multiplicar a quantidade necessária pelo preço unitario das peças e somar com a mdo
        return precoMaoDeObra;
    }

    private void validar(Integer quantidadeNecessaria,
                         BigDecimal precoMaoDeObra,
                         UUID ordemServicoId,
                         UUID servicoId) {

        if (quantidadeNecessaria == null || quantidadeNecessaria <= 0)
            throw new RegraNegocioException("Quantidade necessária é obrigatória");

        if (precoMaoDeObra == null || precoMaoDeObra.compareTo(BigDecimal.ZERO) <= 0)
            throw new RegraNegocioException("Preço de mão de obra é obrigatório");

        if (ordemServicoId == null)
            throw new RegraNegocioException("Ordem de serviço obrigatória");

        if (servicoId == null)
            throw new RegraNegocioException("Serviço obrigatório");
    }
}