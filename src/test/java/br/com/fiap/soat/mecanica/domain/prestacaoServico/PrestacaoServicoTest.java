package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrestacaoServicoTest {

    @Nested
    @DisplayName("Construtor")
    class Construtor {

        @Test
        @DisplayName("Deve criar PrestacaoServico com dados válidos")
        void deveCriar_quandoDadosValidos() {
            // Arrange & Act
            PrestacaoServico ps = new PrestacaoServico(new BigDecimal("200.00"), UUID.randomUUID(), UUID.randomUUID());

            // Assert
            assertThat(ps.getPrecoMaoDeObra()).isEqualByComparingTo(new BigDecimal("200.00"));
            assertThat(ps.getSubtotal()).isEqualByComparingTo(new BigDecimal("200.00"));
            assertThat(ps.getDataInicio()).isNull();
            assertThat(ps.getDataFim()).isNull();
        }

        @Test
        @DisplayName("Deve lançar exceção quando precoMaoDeObra é nulo")
        void deveLancarExcecao_quandoPrecoNulo() {
            assertThatThrownBy(() -> new PrestacaoServico(null, UUID.randomUUID(), UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando ordemServicoId é nulo")
        void deveLancarExcecao_quandoOsIdNulo() {
            assertThatThrownBy(() -> new PrestacaoServico(new BigDecimal("200"), null, UUID.randomUUID()))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando servicoId é nulo")
        void deveLancarExcecao_quandoServicoIdNulo() {
            assertThatThrownBy(() -> new PrestacaoServico(new BigDecimal("200"), UUID.randomUUID(), null))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruir")
    class Reconstruir {

        @Test
        @DisplayName("Deve reconstruir PrestacaoServico")
        void deveReconstruir_quandoDadosCompletos() {
            // Act
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();

            // Assert
            assertThat(ps.getId()).isNotNull();
            assertThat(ps.isAtivo()).isTrue();
        }
    }

    @Nested
    @DisplayName("IniciarServico")
    class IniciarServico {

        @Test
        @DisplayName("Deve iniciar serviço quando OS em execução e ativo e não iniciado")
        void deveIniciar_quandoCondicoesValidas() {
            // Arrange
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();

            // Act
            ps.iniciarServico(true);

            // Assert
            assertThat(ps.getDataInicio()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção quando OS não em execução")
        void deveLancarExcecao_quandoOsNaoEmExecucao() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
            assertThatThrownBy(() -> ps.iniciarServico(false))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando prestação inativa")
        void deveLancarExcecao_quandoInativa() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoInativa();
            assertThatThrownBy(() -> ps.iniciarServico(true))
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando já iniciada")
        void deveLancarExcecao_quandoJaIniciada() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoIniciada();
            assertThatThrownBy(() -> ps.iniciarServico(true))
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("FinalizarServico")
    class FinalizarServico {

        @Test
        @DisplayName("Deve finalizar serviço quando iniciado")
        void deveFinalizar_quandoIniciado() {
            // Arrange
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoIniciada();

            // Act
            ps.finalizarServico();

            // Assert
            assertThat(ps.getDataFim()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar exceção quando não iniciado")
        void deveLancarExcecao_quandoNaoIniciado() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
            assertThatThrownBy(ps::finalizarServico)
                    .isInstanceOf(RegraNegocioException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando já finalizado")
        void deveLancarExcecao_quandoJaFinalizado() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoFinalizada();
            assertThatThrownBy(ps::finalizarServico)
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Validar pode alterar")
    class ValidarPodeAlterar {

        @Test
        @DisplayName("Deve validar quando ativa")
        void deveValidar_quandoAtiva() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
            ps.validarPodeAlterar();
            assertThat(ps.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando inativa")
        void deveLancarExcecao_quandoInativa() {
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoInativa();
            assertThatThrownBy(ps::validarPodeAlterar)
                    .isInstanceOf(RegraNegocioException.class);
        }
    }

    @Nested
    @DisplayName("Valor Peça")
    class ValorPeca {

        @Test
        @DisplayName("Deve adicionar valor de peça ao subtotal")
        void deveAdicionarValorPeca() {
            // Arrange
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
            BigDecimal subtotalInicial = ps.getSubtotal();

            // Act
            BigDecimal valorAdicionado = ps.adicionarValorPeca(new BigDecimal("50.00"), 2);

            // Assert
            assertThat(valorAdicionado).isEqualByComparingTo(new BigDecimal("100.00"));
            assertThat(ps.getSubtotal()).isEqualByComparingTo(subtotalInicial.add(new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve remover valor de peça do subtotal")
        void deveRemoverValorPeca() {
            // Arrange
            PrestacaoServico ps = TestDataFactory.criarPrestacaoServicoValida();
            ps.adicionarValorPeca(new BigDecimal("50.00"), 2);
            BigDecimal subtotalAposAdicao = ps.getSubtotal();

            // Act
            BigDecimal valorRemovido = ps.removerValorPeca(new BigDecimal("50.00"), 2);

            // Assert
            assertThat(valorRemovido).isEqualByComparingTo(new BigDecimal("100.00"));
            assertThat(ps.getSubtotal()).isEqualByComparingTo(subtotalAposAdicao.subtract(new BigDecimal("100.00")));
        }
    }
}
