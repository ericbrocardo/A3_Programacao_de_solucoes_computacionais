
  # Cria o database chamado estoque
CREATE DATABASE IF NOT EXISTS estoque;
USE estoque;

 # cria a tabela de categoria 
CREATE TABLE IF NOT EXISTS categoria (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL UNIQUE,
    tamanho   ENUM('PEQUENO', 'MEDIO', 'GRANDE') NOT NULL,
    embalagem ENUM('LATA', 'VIDRO', 'PLASTICO')  NOT NULL
);

 # cria a tabela do produto 
CREATE TABLE IF NOT EXISTS produto (
    id           INT           AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(150)  NOT NULL UNIQUE,
    preco        DECIMAL(10,2) NOT NULL,
    unidade      VARCHAR(20)   NOT NULL,
    qtd_estoque  DECIMAL(10,3) NOT NULL DEFAULT 0,
    qtd_minima   DECIMAL(10,3) NOT NULL,
    qtd_maxima   DECIMAL(10,3) NOT NULL,
    categoria_id INT           NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

 # cria a tabela de movimentacao 
CREATE TABLE IF NOT EXISTS movimentacao (
    id         INT           AUTO_INCREMENT PRIMARY KEY,
    produto_id INT           NOT NULL,
    data       DATE          NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL,
    tipo       ENUM('ENTRADA','SAIDA') NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

