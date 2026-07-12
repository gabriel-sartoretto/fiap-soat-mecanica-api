package br.com.fiap.soat.mecanica.support;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

public final class DatabaseTestFixtures {

    private static final String PASSWORD_HASH = "$2a$10$fy/vj/iW/AukFhszMTR.7O5iJB707EfVk6zGsZEumVj4hmdgATyu6";

    private DatabaseTestFixtures() {
    }

    public static UUID insertUsuario(JdbcTemplate jdbcTemplate) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO usuarios (id, status, nome, email, senha, cargo_enum)
                VALUES (?, 'ATIVO', ?, ?, ?, 'MECANICO')
                """,
                id,
                "Usuario " + shortId(id),
                "usuario-" + shortId(id) + "@test.local",
                PASSWORD_HASH);
        return id;
    }

    public static UUID insertCliente(JdbcTemplate jdbcTemplate, UUID usuarioId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO clientes (id, status, nome, cpf, cnpj, email, telefone, usuario_id)
                VALUES (?, 'ATIVO', ?, ?, NULL, ?, '11999999999', ?)
                """,
                id,
                "Cliente " + shortId(id),
                cpf(id),
                "cliente-" + shortId(id) + "@test.local",
                usuarioId);
        return id;
    }

    public static UUID insertVeiculo(JdbcTemplate jdbcTemplate, UUID clienteId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO veiculos (id, status, placa, marca, modelo, ano, quantidade_eixos, cliente_id)
                VALUES (?, 'ATIVO', ?, 'Fixture', 'Fixture', '2026', 2, ?)
                """,
                id,
                placa(id),
                clienteId);
        return id;
    }

    public static UUID insertPeca(JdbcTemplate jdbcTemplate) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO pecas (id, status, nome, marca, valor_unitario, quantidade_estoque)
                VALUES (?, 'ATIVO', ?, 'Fixture', ?, 10)
                """,
                id,
                "Peca " + shortId(id),
                new BigDecimal("10.00"));
        return id;
    }

    public static UUID insertServico(JdbcTemplate jdbcTemplate) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO servicos (id, status, nome, descricao)
                VALUES (?, 'ATIVO', ?, 'Servico fixture')
                """,
                id,
                "Servico " + shortId(id));
        return id;
    }

    public static UUID insertOrdemServico(JdbcTemplate jdbcTemplate, UUID veiculoId, UUID usuarioId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO ordem_servicos (
                    id, status, situacao, data_recebida, pago, valor_total, observacao, veiculo_id, usuario_id
                )
                VALUES (?, 'ATIVO', 'RECEBIDA', CURRENT_TIMESTAMP, FALSE, ?, 'OS fixture', ?, ?)
                """,
                id,
                new BigDecimal("0.00"),
                veiculoId,
                usuarioId);
        return id;
    }

    public static UUID insertPrestacaoServico(JdbcTemplate jdbcTemplate, UUID ordemServicoId, UUID servicoId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("""
                INSERT INTO prestacao_servicos (id, status, preco_mdo, subtotal, ordem_servico_id, servico_id)
                VALUES (?, 'ATIVO', ?, ?, ?, ?)
                """,
                id,
                new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                ordemServicoId,
                servicoId);
        return id;
    }

    public static VeiculoDependencies insertVeiculoDependencies(JdbcTemplate jdbcTemplate) {
        UUID usuarioId = insertUsuario(jdbcTemplate);
        UUID clienteId = insertCliente(jdbcTemplate, usuarioId);
        return new VeiculoDependencies(usuarioId, clienteId);
    }

    public static OrdemServicoDependencies insertOrdemServicoDependencies(JdbcTemplate jdbcTemplate) {
        VeiculoDependencies veiculoDependencies = insertVeiculoDependencies(jdbcTemplate);
        UUID veiculoId = insertVeiculo(jdbcTemplate, veiculoDependencies.clienteId());
        return new OrdemServicoDependencies(
                veiculoDependencies.usuarioId(),
                veiculoDependencies.clienteId(),
                veiculoId);
    }

    public static PrestacaoServicoDependencies insertPrestacaoServicoDependencies(JdbcTemplate jdbcTemplate) {
        OrdemServicoDependencies ordemServicoDependencies = insertOrdemServicoDependencies(jdbcTemplate);
        UUID ordemServicoId = insertOrdemServico(
                jdbcTemplate,
                ordemServicoDependencies.veiculoId(),
                ordemServicoDependencies.usuarioId());
        UUID servicoId = insertServico(jdbcTemplate);
        return new PrestacaoServicoDependencies(
                ordemServicoDependencies.usuarioId(),
                ordemServicoDependencies.clienteId(),
                ordemServicoDependencies.veiculoId(),
                ordemServicoId,
                servicoId);
    }

    public static AlocacaoPecaDependencies insertAlocacaoPecaDependencies(JdbcTemplate jdbcTemplate) {
        PrestacaoServicoDependencies prestacaoServicoDependencies = insertPrestacaoServicoDependencies(jdbcTemplate);
        UUID prestacaoServicoId = insertPrestacaoServico(
                jdbcTemplate,
                prestacaoServicoDependencies.ordemServicoId(),
                prestacaoServicoDependencies.servicoId());
        UUID pecaId = insertPeca(jdbcTemplate);
        return new AlocacaoPecaDependencies(
                prestacaoServicoDependencies.usuarioId(),
                prestacaoServicoDependencies.clienteId(),
                prestacaoServicoDependencies.veiculoId(),
                prestacaoServicoDependencies.ordemServicoId(),
                prestacaoServicoDependencies.servicoId(),
                prestacaoServicoId,
                pecaId);
    }

    private static String shortId(UUID id) {
        return id.toString().substring(0, 8);
    }

    private static String cpf(UUID id) {
        String base = String.format("%09d", Math.floorMod(id.getMostSignificantBits(), 1_000_000_000L));
        if (base.chars().distinct().count() == 1) {
            base = "123456789";
        }
        int firstDigit = cpfCheckDigit(base);
        int secondDigit = cpfCheckDigit(base + firstDigit);
        return base + firstDigit + secondDigit;
    }

    private static String placa(UUID id) {
        long letters = id.getMostSignificantBits();
        char first = (char) ('A' + Math.floorMod(letters, 26));
        char second = (char) ('A' + Math.floorMod(letters / 26, 26));
        char third = (char) ('A' + Math.floorMod(letters / (26 * 26), 26));
        int digits = (int) Math.floorMod(id.getLeastSignificantBits(), 10_000);
        return "%c%c%c%04d".formatted(first, second, third, digits);
    }

    private static int cpfCheckDigit(String digits) {
        int sum = 0;
        int weight = digits.length() + 1;
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.digit(digits.charAt(i), 10) * (weight - i);
        }
        int digit = 11 - (sum % 11);
        return digit >= 10 ? 0 : digit;
    }

    public record VeiculoDependencies(UUID usuarioId, UUID clienteId) {
    }

    public record OrdemServicoDependencies(UUID usuarioId, UUID clienteId, UUID veiculoId) {
    }

    public record PrestacaoServicoDependencies(
            UUID usuarioId,
            UUID clienteId,
            UUID veiculoId,
            UUID ordemServicoId,
            UUID servicoId) {
    }

    public record AlocacaoPecaDependencies(
            UUID usuarioId,
            UUID clienteId,
            UUID veiculoId,
            UUID ordemServicoId,
            UUID servicoId,
            UUID prestacaoServicoId,
            UUID pecaId) {
    }
}
