package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper.PrestacaoServicoMapper;
import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.projection.TempoMedioServicoProjection;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestacaoServicoRepositoryImplUnitTest {

    @Mock
    private PrestacaoServicoJpaRepository prestacaoServicoJpaRepository;
    @Mock
    private PrestacaoServicoMapper prestacaoServicoMapper;
    @Mock
    private TempoMedioServicoProjection projection;
    @InjectMocks
    private PrestacaoServicoRepositoryImpl repository;

    @Test
    @DisplayName("Deve calcular tempo mÃ©dio por serviÃ§os")
    void deveCalcularTempoMedioPorServicos() {
        UUID servicoId = UUID.randomUUID();
        when(projection.getNomeServico()).thenReturn("Troca de Ã“leo");
        when(projection.getTempoMedioSegundos()).thenReturn(3600.0);
        when(prestacaoServicoJpaRepository.calcularTempoMedioPorServicos(Set.of(servicoId)))
                .thenReturn(List.of(projection));

        List<TempoMedioServicoResult> resultado = repository.calcularTempoMedioPorServicos(Set.of(servicoId));

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nomeServico()).isEqualTo("Troca de Ã“leo");
        assertThat(resultado.get(0).tempoMedioSegundos()).isEqualTo(3600.0);
    }
}
