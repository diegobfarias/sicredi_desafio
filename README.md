- Descrição da aplicação desenvolvida:
- https://1drv.ms/b/s!Aphkhg3qNBZbjoxshjNgHqvz0eX38g?e=AK07Za

# Desafio Técnico - Sicredi/SoftDesign

## Descrição

Esta aplicação é uma API REST responsável em: criar novas pautas, abrir uma nova sessão de votação, receber votos dos
associados e de contabilizar os votos totais de uma respectiva pauta. O código foi desenvolvido em inglês. Não foi
possível subir a aplicação para o Heroku em virtude de um erro com MongoDb. Foi desenvolvida a solução com mensageria
Kafka, entretanto não ficou com bom desempenho e foi retirada da versão final, mas pode ser encontrada nos commits da
aplicação.

A versão final foi desenvolvida com todos os endpoints solicitados e com dados persistidos no banco de dados MongoDB.
Além disso, foi realizada a tarefa bônus de verificação de CPF consumindo uma API externa e parcialmente de mensageria
com Kafka. Os testes unitários desenvolvidos contemplam todos os cenários possíveis.

## Pauta

A pauta foi modelada em inglês como "Topic" e possui como atributos uma ID, uma data de início e fim da sessão, uma
descrição da pauta e também um Map que faz a relação de chave do associado (CPF) e seu voto (Sim/Não)

## Especificações Técnicas

Este projeto foi desenvolvido na linguagem Java, utilizando SpringBoot e MongoDB como banco de dados. Ela está rodando
em um servidor local. Foi utilizado JUnit 5 para a realização de testes unitários.

## Execução

Para executar a aplicação basta roda-la locamente. Subirá no endereço http://localhost:8080

## Endpoints

Os endpoints disponibilizados pela API são:

    POST: http://localhost:8080/pauta/criar:
    Recebe um body que contém as informações supracitadas da Pauta para criar uma nova pauta. No body a ID pode ser 
    nula pois o banco gerará uma automática. A data de início e fim da sessão da pauta também podem ser nulas, neste
    caso será atribuída uma sessão de 1 minuto de duração. A pauta pode ser criada com ou sem votos de associados.

    PUT: /http://localhost:8080/pauta/{idPauta}/votar?{idAssociado}=02600592008&{votoAssociado}=Sim:
    Especifica no caminho o ID da pauta e informa o CPF e o voto do associado para computar o voto do associado. Será
    verificado se o associado possui um CPF válido e se for válido se ele pode votar ou não. Além disso, também será
    verificado se ele já votou na pauta em questão. Por fim, será verificado se a pauta em questão ainda está com sessão
    aberta para votação.

    GET: http://localhost:8080/pauta/{idPauta}/contar-votos:
    Especifica no caminho o ID da pauta e retorna um body com a descrição da pauta e quantidade de votos SIM e NÃO

    PUT: http://localhost:8080/pauta/{idPauta}/nova-sessao-votacao?startTopic=2022-09-14T18:11:26.421&endTopic=2022-09-15T17:43:40.421
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