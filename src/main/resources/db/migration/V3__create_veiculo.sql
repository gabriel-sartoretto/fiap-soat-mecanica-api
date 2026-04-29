CREATE TABLE veiculos
(
    id               UUID PRIMARY KEY,
    status     VARCHAR(20)  NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    placa            VARCHAR(7)  NOT NULL UNIQUE,
    marca            VARCHAR(255) NOT NULL,
    modelo           VARCHAR(255) NOT NULL,
    ano              VARCHAR(4)   NOT NULL,
    quantidade_eixos INT          NOT NULL,
    cliente_id       UUID         NOT NULL,

    CONSTRAINT fk_veiculo_cliente
        FOREIGN KEY (cliente_id)
            REFERENCES clientes (id)
);

CREATE INDEX idx_veiculo_cliente_id ON veiculos(cliente_id);