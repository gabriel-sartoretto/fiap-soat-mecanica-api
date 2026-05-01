CREATE TABLE servicos
(
    id        UUID PRIMARY KEY,
    status    VARCHAR(20)  NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    nome      VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);