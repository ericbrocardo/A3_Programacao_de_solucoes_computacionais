## Controle de Estoque A3

Sistema de gerenciamento de estoque desenvolvido como projeto acadêmico da disciplina de Problemas e Soluções Computacionais.

## Sobre

Sistema desenvolvido para atender as necessidades de controle de estoque de uma empresa de comércio fictícia. 
O sistema permite que o comerciante mantenha um registro atualizado de todos os seus produtos, 
organizados por categorias, refletindo em tempo real a situação do estoque.

A cada movimentação de entrada ou saída, o saldo do produto é atualizado automaticamente, 
com alertas quando a quantidade estiver abaixo do mínimo ou acima do máximo definidos. 
Além disso, o sistema oferece relatórios gerenciais para apoiar a tomada de decisão nas compras e no planejamento do estoque.

## Requisitos Funcionais

- RF 001: O sistema deve permitir cadastrar, editar, consultar e excluir produtos (CRUD) 
- RF 002: O sistema deve permitir cadastrar, editar, consultar e excluir categorias (CRUD) 
- RF 003: O sistema deve registrar movimentações de estoque do tipo entrada ou saída 
- RF 004: O sistema deve atualizar automaticamente a quantidade em estoque a cada movimentação 
- RF 005: O sistema deve alertar quando a quantidade do produto estiver abaixo do mínimo na saída 
- RF 006: O sistema deve alertar quando a quantidade do produto estiver acima do máximo na entrada 
- RF 007: O sistema deve permitir reajustar o preço de todos os produtos por um percentual informado 
- RF 008: O sistema deve gerar relatório de lista de preços em ordem alfabética 
- RF 009: O sistema deve gerar relatório de balanço físico e financeiro do estoque 
- RF 010: O sistema deve gerar relatório dos produtos abaixo da quantidade mínima
- RF 011: O sistema deve gerar relatório da quantidade de produtos distintos por categoria 
- RF 012: O sistema deve gerar relatório do produto com maior número de entradas e saídas 

## Requisitos Não Funcionais

- RNF 001: O sistema foi desenvolvido na linguagem Java (JDK 25) 
- RNF 002: O sistema deve utilizar o banco de dados MySQL 8.0 
- RNF 003: A conexão com o banco de dados deve ser feita via JDBC utilizando MySQL Connector/J 8.3.0 
- RNF 004: O sistema deve seguir o padrão de projeto DAO para acesso aos dados 
- RNF 005: O código deve ser organizado em pacotes por responsabilidade (dao, modelo, principal, visao) 
- RNF 006: O sistema deve ser gerenciado com Apache Maven 3.9.12 
- RNF 007: O código-fonte deve seguir padrões de nomenclatura e estar devidamente comentado
- RNF 008: O desenvolvimento deve ser colaborativo utilizando GitHub com commits frequentes e descritivos 

## Tecnologias Utilizadas

- Java (JDK)  ->  Versão: 25  ->  Descrição: Linguagem principal de desenvolvimento           
- Apache NetBeans IDE  ->  Versão: 29  ->  Descrição: Ambiente de desenvolvimento integrado (IDE)      
- MySQL  ->  Versão: 8.0  ->  Descrição: Sistema gerenciador de banco de dados relacional 
- MySQL Connector/J  ->  Versão: 8.3.0  ->  Descrição: Driver JDBC para conexão Java com o MySQL        
- Apache Maven  ->  Versão: 3.9.12  ->  Descrição: Gerenciador de dependências e build do projeto   

## Integrantes do Projeto

Arthur Coelho
RA: 1072520405
Github: arthurcoelhooo

Bruno Franz Zeferino 
RA:10726111918
Github: Bruno-F-Zeferino

Eric Brocardo Freire 
RA: 10726112597
Github:ericbrocardo

Nathan Pierre da Silva
RA: 1072617023
Github: Nathan-Pierre

Vinicius Weisweiler
RA: 1072616210
Github: viniciusw2408-lang


