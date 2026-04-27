CREATE TABLE veiculos (
    id UUID PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    ano VARCHAR(4) NOT NULL,
    quantidade_eixos INT NOT NULL
);