INSERT INTO usuarios (id, status, nome, email, senha, cargo_enum)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'ATIVO', 'Atendente Teste', 'atendente@mecanica.test', '$2a$10$fy/vj/iW/AukFhszMTR.7O5iJB707EfVk6zGsZEumVj4hmdgATyu6', 'ATENDENTE'),
    ('22222222-2222-2222-2222-222222222222', 'ATIVO', 'Mecanico Teste', 'mecanico@mecanica.test', '$2a$10$fy/vj/iW/AukFhszMTR.7O5iJB707EfVk6zGsZEumVj4hmdgATyu6', 'MECANICO'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ATIVO', 'Almoxarife Teste', 'almoxarife@mecanica.test', '$2a$10$fy/vj/iW/AukFhszMTR.7O5iJB707EfVk6zGsZEumVj4hmdgATyu6', 'ALMOXARIFE');

INSERT INTO clientes (id, status, nome, cpf, cnpj, email, telefone, usuario_id)
VALUES
    ('33333333-3333-3333-3333-333333333333', 'ATIVO', 'Cliente PF Teste', '52998224725', NULL, 'cliente.pf@mecanica.test', '11987654321', '11111111-1111-1111-1111-111111111111'),
    ('33333333-3333-3333-3333-333333333334', 'ATIVO', 'Cliente PJ Teste', NULL, '11222333000181', 'cliente.pj@mecanica.test', '1133334444', '11111111-1111-1111-1111-111111111111');

INSERT INTO veiculos (id, status, placa, marca, modelo, ano, quantidade_eixos, cliente_id)
VALUES
    ('44444444-4444-4444-4444-444444444444', 'ATIVO', 'ABC1D23', 'Toyota', 'Corolla', '2020', 2, '33333333-3333-3333-3333-333333333333'),
    ('44444444-4444-4444-4444-444444444445', 'ATIVO', 'XYZ1234', 'Ford', 'Cargo', '2019', 3, '33333333-3333-3333-3333-333333333334');

INSERT INTO pecas (id, status, nome, marca, valor_unitario, quantidade_estoque)
VALUES
    ('55555555-5555-5555-5555-555555555555', 'ATIVO', 'Filtro de Oleo', 'Bosch', 39.90, 20),
    ('55555555-5555-5555-5555-555555555556', 'ATIVO', 'Pastilha de Freio', 'Fras-le', 129.90, 12),
    ('55555555-5555-5555-5555-555555555557', 'ATIVO', 'Oleo 5W30', 'Shell', 49.90, 30);

INSERT INTO servicos (id, status, nome, descricao)
VALUES
    ('77777777-7777-7777-7777-777777777777', 'ATIVO', 'Troca de oleo', 'Substituicao de oleo e filtro'),
    ('77777777-7777-7777-7777-777777777778', 'ATIVO', 'Revisao de freios', 'Inspecao e manutencao do sistema de freios'),
    ('77777777-7777-7777-7777-777777777779', 'ATIVO', 'Alinhamento', 'Alinhamento de direcao e geometria');

INSERT INTO ordem_servicos (
    id,
    status,
    situacao,
    data_recebida,
    data_diagnostico,
    data_aguardando_aprovacao,
    data_execucao,
    data_finalizada,
    data_entregue,
    pago,
    valor_total,
    observacao,
    veiculo_id,
    usuario_id
)
VALUES
    (
        '66666666-6666-6666-6666-666666666666',
        'ATIVO',
        'EM_DIAGNOSTICO',
        '2026-05-02 09:00:00',
        '2026-05-02 10:00:00',
        NULL,
        NULL,
        NULL,
        NULL,
        FALSE,
        189.90,
        'Barulho ao frear',
        '44444444-4444-4444-4444-444444444444',
        '22222222-2222-2222-2222-222222222222'
    ),
    (
        '66666666-6666-6666-6666-666666666667',
        'ATIVO',
        'FINALIZADA',
        '2026-05-01 08:00:00',
        '2026-05-01 09:00:00',
        '2026-05-01 10:00:00',
        '2026-05-01 11:00:00',
        '2026-05-01 12:00:00',
        NULL,
        FALSE,
        519.80,
        'Troca de oleo e revisao de freios',
        '44444444-4444-4444-4444-444444444445',
        '22222222-2222-2222-2222-222222222222'
    );

INSERT INTO prestacao_servicos (
    id,
    status,
    preco_mdo,
    subtotal,
    data_inicio,
    data_fim,
    ordem_servico_id,
    servico_id
)
VALUES
    (
        '88888888-8888-8888-8888-888888888888',
        'ATIVO',
        150.00,
        189.90,
        NULL,
        NULL,
        '66666666-6666-6666-6666-666666666666',
        '77777777-7777-7777-7777-777777777777'
    ),
    (
        '88888888-8888-8888-8888-888888888889',
        'ATIVO',
        260.00,
        519.80,
        '2026-05-01 10:00:00',
        '2026-05-01 12:00:00',
        '66666666-6666-6666-6666-666666666667',
        '77777777-7777-7777-7777-777777777778'
    );

INSERT INTO alocacao_pecas (id, status, quantidade_necessaria, prestacao_servico_id, peca_id)
VALUES
    ('99999999-9999-9999-9999-999999999999', 'ATIVO', 1, '88888888-8888-8888-8888-888888888888', '55555555-5555-5555-5555-555555555555'),
    ('99999999-9999-9999-9999-999999999998', 'ATIVO', 2, '88888888-8888-8888-8888-888888888889', '55555555-5555-5555-5555-555555555556');
