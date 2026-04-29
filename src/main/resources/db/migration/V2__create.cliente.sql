CREATE TABLE clientes
(
    id         UUID PRIMARY KEY,
    status     VARCHAR(20)  NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    nome       VARCHAR(255) NOT NULL,
    cpf        VARCHAR(11) UNIQUE,
    cnpj       VARCHAR(14) UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    telefone   VARCHAR(20),
    usuario_id UUID         NOT NULL,

    CONSTRAINT uk_cliente_cpf UNIQUE (cpf),
    CONSTRAINT uk_cliente_cnpj UNIQUE (cnpj),
    CONSTRAINT uk_cliente_email UNIQUE (email),

    CONSTRAINT chk_cliente_documento
        CHECK (
            (cpf IS NOT NULL AND cnpj IS NULL)
                OR
            (cpf IS NULL AND cnpj IS NOT NULL)
            )
);

CREATE INDEX idx_cliente_usuario_id ON clientes(usuario_id);