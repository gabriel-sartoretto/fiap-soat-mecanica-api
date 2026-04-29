CREATE TABLE usuarios
(
    id         UUID PRIMARY KEY,
    status     VARCHAR(20)  NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    nome       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    senha      VARCHAR(255) NOT NULL,
    cargo_enum VARCHAR(30)  NOT NULL CHECK (cargo_enum IN ('MECANICO', 'ATENDENTE', 'ALMOXARIFE'))
);