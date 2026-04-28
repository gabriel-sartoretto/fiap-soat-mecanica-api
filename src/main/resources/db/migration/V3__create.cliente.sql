CREATE TABLE clientes (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    cnpj VARCHAR(14) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    usuario_id UUID NOT NULL
);