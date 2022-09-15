# Desafio Técnico - Sicredi/SoftDesign

## Descrição

Esta aplicação é uma API REST responsável em: criar novas pautas, abrir uma nova sessão de votação, receber votos dos
associados e de contabilizar os votos totais de uma respectiva pauta. O código foi desenvolvido em inglês. Não foi
possível subir a aplicação para o Heroku em virtude de que consegui realizar a implementação do Kafka apenas local,
todavia sem o Kafka conseguiria estar com a aplicação no Heroku.

## Pauta

A pauta foi modelada em inglês como "Topic" e possui como atributos uma ID, uma data de início e fim da sessão, uma
descrição da pauta e também um Map que faz a relação de chave do associado (CPF) e seu voto (Sim/Não)

## Especificações Técnicas

Este projeto foi desenvolvido na linguagem Java, utilizando SpringBoot e MongoDB como banco de dados. Ela está rodando
em um servidor na nuvem da Heroku. Foi utilizado JUnit 5 para a realização de testes unitários.

## Execução

A versão do Kafka utilizada é kafka_2.12-2.6.2. Para subir, é necessário dois terminais bash com os seguintes comandos:

- bin/windows/zookeeper-server-start.bat config/zookeeper.properties
- bin/windows/kafka-server-start.bat config/server.properties

## Endpoints

Os endpoints disponibilizados pela API são:

    POST: /pauta/criar:
    Recebe um body que contém as informações supracitadas da Pauta para criar uma nova pauta

    PUT: /pauta/{topicId}/votar?associateId=...&associateVote=...:
    Especifica no caminho o ID da pauta e informa o CPF e o voto do associado para computar o voto do associado

    GET: /pauta/{topicId}/contar-votos:
    Especifica no caminho o ID da pauta e retorna um body com a descrição da pauta e quantidade de votos SIM e NÃO

    PUT: /pauta/{topicId}/nova-sessao-votacao:
    Especifica no caminho o ID da pauta e tem como paramêtros opcionais o início e o fim da sessão. Caso não seja 
    informado essas datas, será atribuído a data de início como o momento atual e a data de fim para 1 minuto a partir
    do momento atual.

## Repositório

- https://github.com/diegobfarias/sicredi_desafio

## Contato

Essa aplicação foi desenvolvida por:

- Autor: Diego Bergonsi de Farias
- LinkedIn: https://www.linkedin.com/in/diegobfarias/
- E-mail: fariasdb@outlook.com
- Telefone: 51 99887-7185