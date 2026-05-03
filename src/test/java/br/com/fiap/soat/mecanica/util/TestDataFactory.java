package br.com.fiap.soat.mecanica.util;

import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.enums.SituacaoOrdemServicoEnum;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.*;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataFactory {

    public static final String CPF_VALIDO = "52998224725";
    public static final String CNPJ_VALIDO = "11222333000181";
    public static final String EMAIL_VALIDO = "teste@email.com";
    public static final String TELEFONE_VALIDO = "11999887766";
    public static final String PLACA_ANTIGA = "ABC1234";
    public static final String PLACA_MERCOSUL = "ABC1D23";
    public static final String SENHA_VALIDA = "Senha@123";

    public static Usuario criarUsuarioMecanico() {
        return Usuario.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Mecânico Teste",
                "$2a$10$encodedPassword",
                new Email(EMAIL_VALIDO),
                CargoEnum.MECANICO
        );
    }

    public static Usuario criarUsuarioAtendente() {
        return Usuario.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Atendente Teste",
                "$2a$10$encodedPassword",
                new Email("atendente@email.com"),
                CargoEnum.ATENDENTE
        );
    }

    public static Usuario criarUsuarioAlmoxarife() {
        return Usuario.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Almoxarife Teste",
                "$2a$10$encodedPassword",
                new Email("almoxarife@email.com"),
                CargoEnum.ALMOXARIFE
        );
    }

    public static Cliente criarClienteComCpf() {
        UUID usuarioId = UUID.randomUUID();
        return Cliente.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Cliente CPF",
                new CPF(CPF_VALIDO),
                null,
                new Email(EMAIL_VALIDO),
                new Telefone(TELEFONE_VALIDO),
                usuarioId
        );
    }

    public static Cliente criarClienteComCnpj() {
        UUID usuarioId = UUID.randomUUID();
        return Cliente.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Cliente CNPJ",
                null,
                new CNPJ(CNPJ_VALIDO),
                new Email("cnpj@email.com"),
                new Telefone(TELEFONE_VALIDO),
                usuarioId
        );
    }

    public static Veiculo criarVeiculoValido() {
        return Veiculo.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                new Placa(PLACA_ANTIGA),
                "Toyota",
                "Corolla",
                "2023",
                2,
                UUID.randomUUID()
        );
    }

    public static Peca criarPecaValida() {
        return Peca.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Pastilha de Freio",
                "Bosch",
                new BigDecimal("150.00"),
                10
        );
    }

    public static Servico criarServicoValido() {
        return Servico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                "Troca de Óleo",
                "Troca completa de óleo"
        );
    }

    public static OrdemServico criarOrdemServicoRecebida() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.RECEBIDA,
                LocalDateTime.now(),
                null, null, null, null, null,
                null,
                BigDecimal.ZERO,
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoEmDiagnostico() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.EM_DIAGNOSTICO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null, null,
                null,
                BigDecimal.ZERO,
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoAguardandoAprovacao() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.AGUARDANDO_APROVACAO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null,
                null,
                BigDecimal.ZERO,
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoEmExecucao() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.EM_EXECUCAO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null,
                false,
                new BigDecimal("500.00"),
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoFinalizada() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.FINALIZADA,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                false,
                new BigDecimal("500.00"),
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoEntregue() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                SituacaoOrdemServicoEnum.ENTREGUE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                new BigDecimal("500.00"),
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static OrdemServico criarOrdemServicoInativa() {
        return OrdemServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.INATIVO,
                SituacaoOrdemServicoEnum.AGUARDANDO_APROVACAO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null,
                null,
                BigDecimal.ZERO,
                "Observação teste",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static PrestacaoServico criarPrestacaoServicoValida() {
        return PrestacaoServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                new BigDecimal("200.00"),
                new BigDecimal("200.00"),
                null,
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static PrestacaoServico criarPrestacaoServicoIniciada() {
        return PrestacaoServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                new BigDecimal("200.00"),
                new BigDecimal("200.00"),
                LocalDateTime.now(),
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static PrestacaoServico criarPrestacaoServicoFinalizada() {
        return PrestacaoServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                new BigDecimal("200.00"),
                new BigDecimal("200.00"),
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static PrestacaoServico criarPrestacaoServicoInativa() {
        return PrestacaoServico.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.INATIVO,
                new BigDecimal("200.00"),
                new BigDecimal("200.00"),
                null,
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static AlocacaoPeca criarAlocacaoPecaValida() {
        return AlocacaoPeca.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.ATIVO,
                2,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static AlocacaoPeca criarAlocacaoPecaInativa() {
        return AlocacaoPeca.reconstruir(
                UUID.randomUUID(),
                StatusRecursoEnum.INATIVO,
                2,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }
}
