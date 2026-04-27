CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    nome VARCHAR(10) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cargo_enum VARCHAR(50) NOT NULL
);