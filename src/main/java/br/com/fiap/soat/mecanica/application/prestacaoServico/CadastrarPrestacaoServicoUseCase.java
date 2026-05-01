package br.com.fiap.soat.mecanica.application.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarPrestacaoServicoUseCase {

    private final PrestacaoServicoRepository prestacaoServicoRepository;

    public PrestacaoServico executar(Integer quantidadeNecessaria, BigDecimal precoMaoDeObra, UUID ordemServicoId, UUID servicoId) {

        PrestacaoServico prestacaoServico = new PrestacaoServico(quantidadeNecessaria, precoMaoDeObra, ordemServicoId, servicoId);
        return prestacaoServicoRepository.salvar(prestacaoServico);
    }
}