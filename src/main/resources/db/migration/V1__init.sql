-- ==========================
-- CREATE TABLES BASE
-- ==========================

CREATE TABLE categorias_produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias_produto(id)
);

CREATE TABLE restaurantes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(255),
    capacidade INT
);

CREATE TABLE mesas (
    id BIGSERIAL PRIMARY KEY,
    numero INT,
    lugares INT,
    disponivel BOOLEAN DEFAULT TRUE,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_mesa_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telefone VARCHAR(20)
);

CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    mesa_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_pedido_mesa FOREIGN KEY (mesa_id) REFERENCES mesas(id),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    quantidade INT,
    subtotal NUMERIC(10,2),
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

