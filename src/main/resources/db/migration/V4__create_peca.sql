CREATE TABLE pecas (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    nome VARCHAR(255) UNIQUE NOT NULL,
    marca VARCHAR(255) NOT NULL,
    valor_unitario NUMERIC(10,2) NOT NULL,
    quantidade_estoque INT NOT NULL
);
