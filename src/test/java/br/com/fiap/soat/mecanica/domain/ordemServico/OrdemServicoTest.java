package br.com.fiap.soat.mecanica.domain.ordemServico;

import br.com.fiap.soat.mecanica.domain.enums.SituacaoOrdemServicoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdemServicoTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar OrdemServico com situação RECEBIDA")
        void deveCriar_quandoDadosValidos() {
            // Arrange
            UUID veiculoId = UUID.randomUUID();
            UUID usuarioId = UUID.randomUUID();

            // Act
            OrdemServico os = new OrdemServico("Obs", veiculoId, usuarioId);

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.RECEBIDA);
            assertThat(os.getValorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(os.getDataRecebida()).isNotNull();
            assertThat(os.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando veiculoId é nulo")
        void deveLancarExcecao_quandoVeiculoIdNulo() {
            assertThatThrownBy(() -> new OrdemServico("Obs", null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("veículo");
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuarioId é nulo")
        void deveLancarExcecao_quandoUsuarioIdNulo() {
            assertThatThrownBy(() -> new OrdemServico("Obs", UUID.randomUUID(), null))
                    .isInstanceOf(RegraNegocioException.class)
                    .hasMessageContaining("mecânico");
        }
    }

    @Nested
    @DisplayName("Reconstruir")
    class Reconstruir {

        @Test
        @DisplayName("Deve reconstruir OrdemServico com todos os campos")
        void deveReconstruir_quandoDadosCompletos() {
            // Act
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();

            // Assert
            assertThat(os.getId()).isNotNull();
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.EM_EXECUCAO);
        }
    }

    @Nested
    @DisplayName("Transições de estado")
    class TransicoesDeEstado {

        @Test
        @DisplayName("Deve iniciar diagnóstico quando RECEBIDA")
        void deveIniciarDiagnostico_quandoRecebida() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();

            // Act
            os.iniciarDiagnostico();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.EM_DIAGNOSTICO);
            assertThat(os.getDataDiagnostico()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção ao iniciar diagnóstico quando não RECEBIDA")
        void deveLancarExcecao_quandoIniciarDiagnosticoNaoRecebida() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();
            assertThatThrownBy(os::iniciarDiagnostico)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve enviar para aprovação quando EM_DIAGNOSTICO")
        void deveEnviarParaAprovacao_quandoEmDiagnostico() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();

            // Act
            os.enviarParaAprovacao();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.AGUARDANDO_APROVACAO);
            assertThat(os.getDataAguardandoAprovacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção ao enviar para aprovação quando não EM_DIAGNOSTICO")
        void deveLancarExcecao_quandoEnviarAprovacaoNaoEmDiagnostico() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::enviarParaAprovacao)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve iniciar execução quando AGUARDANDO_APROVACAO")
        void deveIniciarExecucao_quandoAguardandoAprovacao() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoAguardandoAprovacao();

            // Act
            os.iniciarExecucao();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.EM_EXECUCAO);
            assertThat(os.getDataExecucao()).isNotNull();
            assertThat(os.getPago()).isFalse();
        }

        @Test
        @DisplayName("Deve lançar exceção ao iniciar execução quando não AGUARDANDO_APROVACAO")
        void deveLancarExcecao_quandoIniciarExecucaoNaoAguardando() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::iniciarExecucao)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve finalizar quando EM_EXECUCAO")
        void deveFinalizar_quandoEmExecucao() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();

            // Act
            os.finalizar();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.FINALIZADA);
            assertThat(os.getDataFinalizada()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção ao finalizar quando não EM_EXECUCAO")
        void deveLancarExcecao_quandoFinalizarNaoEmExecucao() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::finalizar)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve entregar quando FINALIZADA")
        void deveEntregar_quandoFinalizada() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoFinalizada();

            // Act
            os.entregar();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.ENTREGUE);
            assertThat(os.getDataEntregue()).isNotNull();
            assertThat(os.getPago()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao entregar quando não FINALIZADA")
        void deveLancarExcecao_quandoEntregarNaoFinalizada() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();
            assertThatThrownBy(os::entregar)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve voltar para diagnóstico quando AGUARDANDO_APROVACAO")
        void deveVoltarParaDiagnostico_quandoAguardandoAprovacao() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoAguardandoAprovacao();

            // Act
            os.voltarParaDiagnostico();

            // Assert
            assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServicoEnum.EM_DIAGNOSTICO);
        }

        @Test
        @DisplayName("Deve lançar exceção ao voltar diagnóstico quando não AGUARDANDO_APROVACAO")
        void deveLancarExcecao_quandoVoltarDiagnosticoNaoAguardando() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::voltarParaDiagnostico)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve cancelar por desistência quando AGUARDANDO_APROVACAO")
        void deveCancelar_quandoAguardandoAprovacao() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoAguardandoAprovacao();

            // Act
            os.cancelarPorDesistencia();

            // Assert
            assertThat(os.isInativo()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao cancelar quando não AGUARDANDO_APROVACAO")
        void deveLancarExcecao_quandoCancelarNaoAguardando() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::cancelarPorDesistencia)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção em qualquer transição quando OS inativa")
        void deveLancarExcecao_quandoOsInativa() {
            OrdemServico os = TestDataFactory.criarOrdemServicoInativa();

            assertThatThrownBy(os::iniciarDiagnostico).isInstanceOf(RegraNegocioException.class);
            assertThatThrownBy(os::enviarParaAprovacao).isInstanceOf(RegraNegocioException.class);
            assertThatThrownBy(os::iniciarExecucao).isInstanceOf(RegraNegocioException.class);
            assertThatThrownBy(os::finalizar).isInstanceOf(RegraNegocioException.class);
            assertThatThrownBy(os::entregar).isInstanceOf(RegraNegocioException.class);
            assertThatThrownBy(os::cancelarPorDesistencia).isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Valores")
    class Valores {

        @Test
        @DisplayName("Deve adicionar valor ao total")
        void deveAdicionarValor_quandoValorPositivo() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();

            // Act
            os.adicionarValor(new BigDecimal("100.00"));

            // Assert
            assertThat(os.getValorTotal()).isEqualByComparingTo(new BigDecimal("100.00"));
        }

        @Test
        @DisplayName("Deve remover valor do total")
        void deveRemoverValor_quandoValorPositivo() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();
            BigDecimal valorOriginal = os.getValorTotal();

            // Act
            os.removerValor(new BigDecimal("100.00"));

            // Assert
            assertThat(os.getValorTotal()).isEqualByComparingTo(valorOriginal.subtract(new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve não deixar valor total negativo ao remover")
        void deveManterZero_quandoRemoverMaisQueTotal() {
            // Arrange
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();

            // Act
            os.removerValor(new BigDecimal("1000.00"));

            // Assert
            assertThat(os.getValorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é nulo")
        void deveLancarExcecao_quandoValorNulo() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(() -> os.adicionarValor(null))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é negativo")
        void deveLancarExcecao_quandoValorNegativo() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(() -> os.adicionarValor(new BigDecimal("-1")))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Validações de prestação de serviço")
    class ValidacoesPrestacao {

        @Test
        @DisplayName("Deve permitir cadastrar prestação quando RECEBIDA")
        void devePermitir_quandoRecebida() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            os.validarPermiteCadastrarPrestacaoServico();
            assertThat(os.isRecebida()).isTrue();
        }

        @Test
        @DisplayName("Deve permitir cadastrar prestação quando EM_DIAGNOSTICO")
        void devePermitir_quandoEmDiagnostico() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();
            os.validarPermiteCadastrarPrestacaoServico();
            assertThat(os.isEmDiagnostico()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando não permite cadastrar prestação")
        void deveLancarExcecao_quandoNaoPermiteCadastrar() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();
            assertThatThrownBy(os::validarPermiteCadastrarPrestacaoServico)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve validar que permite alterar diagnóstico")
        void deveValidar_quandoEmDiagnostico() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmDiagnostico();
            os.validarPermiteAlterarDiagnostico();
            assertThat(os.isEmDiagnostico()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando não está em diagnóstico")
        void deveLancarExcecao_quandoNaoEmDiagnostico() {
            OrdemServico os = TestDataFactory.criarOrdemServicoRecebida();
            assertThatThrownBy(os::validarPermiteAlterarDiagnostico)
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Status checks")
    class StatusChecks {

        @Test
        @DisplayName("Deve retornar true para isAguardandoAprovacao")
        void deveRetornarTrue_quandoAguardandoAprovacao() {
            OrdemServico os = TestDataFactory.criarOrdemServicoAguardandoAprovacao();
            assertThat(os.isAguardandoAprovacao()).isTrue();
        }

        @Test
        @DisplayName("Deve retornar true para isEmExecucao")
        void deveRetornarTrue_quandoEmExecucao() {
            OrdemServico os = TestDataFactory.criarOrdemServicoEmExecucao();
            assertThat(os.isEmExecucao()).isTrue();
        }

        @Test
        @DisplayName("Deve retornar true para isFinalizada")
        void deveRetornarTrue_quandoFinalizada() {
            OrdemServico os = TestDataFactory.criarOrdemServicoFinalizada();
            assertThat(os.isFinalizada()).isTrue();
        }
    }
}
