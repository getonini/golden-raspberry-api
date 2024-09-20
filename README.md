# Golden Raspberry Awards API

Esta é uma API RESTful que processa a lista de indicados e vencedores da categoria "Pior Filme" do **Golden Raspberry Awards**. A API permite obter informacões sobre o produtor com o maior intervalo entre dois prêmios consecutivos e o produtor que ganhou dois prêmios mais rapidamente.

## Funcionalidades
- Ler o arquivo CSV dos filmes e inserir os dados no banco de dados ao iniciar a aplicacao.
- API para retornar o produtor com o maior e o menor intervalo entre prêmios.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.1.4**
- **H2 Database** (banco de dados em memoria)
- **JPA / Hibernate** (para mapeamento objeto-relacional)
- **OpenCSV** (para ler o arquivo CSV)
- **JUnit 5** (para testes de integracao)
- **Maven** (para gerenciamento de dependências e build)

## Pré-requisitos

Certifique-se de ter instalado:
- **Java 17**
- **Maven**

## Como Rodar o Projeto

1. Clone este repositorio:

   ```bash
   git clone https://github.com/seu-usuario/golden-raspberry-api.git
   cd golden-raspberry-api

2. Execute o seguinte comando para compilar e rodar a aplicacao:

    ```bash
    mvn spring-boot:run

A aplicacao sera iniciada e estara disponivel em http://localhost:8080

3. Para testar a API, você pode acessar os seguintes endpoints:

Obter o produtor com maior e menor intervalo entre prêmios:
GET http://localhost:8080/awards/intervals

4. Para acessar o console do banco de dados H2 (opcional):

Va para http://localhost:8080/h2-console e use as seguintes credenciais:
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (em branco)


## Como Executar os Testes de Integracao
Para rodar todos os testes de integracao, use o comando:

    mvn test

Isso executara os testes de integracao para garantir que a API esta funcionando corretamente com base no conjunto de dados.