# PulsePay — Carteira Digital

## 1. Sobre o projeto

O **PulsePay** é um sistema de carteira digital desenvolvido como projeto acadêmico da equipe PulsePay.

A aplicação permite cadastrar usuários, realizar depósitos, transferir valores entre contas, consultar o histórico de movimentações e receber notificações em tempo real sobre transferências recebidas.

## 2. Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Spring Web
- Spring WebSocket
- PostgreSQL 18
- Maven
- HTML, CSS e JavaScript

## 3. Funcionalidades

- Cadastro de usuários e contas.
- Validação básica dos dados cadastrais e do CPF.
- Consulta de contas cadastradas.
- Depósitos em contas.
- Transferências entre contas.
- Consulta de saldo.
- Extrato de movimentações.
- Registro de auditoria das operações.
- Notificações em tempo real utilizando WebSocket.

## 4. Requisitos para execução

Antes de executar o projeto, é necessário ter instalado:

- JDK 21 ou superior.
- PostgreSQL.
- Maven.

## 5. Configuração do banco de dados

O projeto utiliza PostgreSQL.

Crie um banco de dados chamado:

`pulsepay_db`

A configuração de conexão deve utilizar:

- Banco: `pulsepay_db`
- Endereço: `localhost`
- Porta: `5432`

Verifique as credenciais de acesso ao PostgreSQL no arquivo `application.properties` antes de executar a aplicação.

## 6. Como executar o projeto

### Opção 1 — Executar pelo Maven

Na pasta principal do projeto, execute:

```bash
mvn clean package
```

Depois:

```bash
mvn spring-boot:run
```

### Opção 2 — Executar pelo JAR

Gere o pacote:

```bash
mvn clean package
```

Execute:

```bash
java -jar target/pulsepay-0.0.1-SNAPSHOT.jar
```

Após iniciar a aplicação, acesse:

http://localhost:8080

## 7. Endpoints da API

### Contas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/accounts` | Cadastrar conta |
| GET | `/api/accounts` | Listar contas |
| GET | `/api/accounts/{id}` | Consultar conta |
| POST | `/api/accounts/{id}/deposit` | Realizar depósito |
| POST | `/api/accounts/transfer` | Realizar transferência |

### Movimentações

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/operations` | Consultar movimentações |
| GET | `/api/operations/account/{accountId}` | Consultar extrato da conta |

### Transferências

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/transfers` | Consultar transferências registradas |

## 8. Notificações em tempo real

O sistema utiliza WebSocket com STOMP para notificar o recebimento de transferências.

- Endpoint de conexão: `/ws`
- Canal de notificações: `/topic/accounts/{receiverId}`

Quando uma transferência é recebida, o sistema envia uma notificação para a conta de destino.

## 9. Regras de negócio

O sistema implementa validações para:

- Impedir o cadastro de CPF duplicado.
- Impedir o cadastro de e-mail duplicado.
- Validar os dígitos verificadores do CPF.
- Impedir depósitos com valores iguais ou inferiores a zero.
- Impedir transferências com valores inválidos.
- Impedir transferências para a própria conta.
- Impedir transferências sem saldo suficiente.
- Registrar as movimentações realizadas.

## 10. Auditoria

As operações são registradas no histórico do sistema, permitindo consultar eventos como:

- Cadastro de conta.
- Depósitos.
- Transferências realizadas.
- Transferências recebidas.

## 11. Estrutura do projeto

```text
src/main/java/br/com/pulsepay
├── config
├── controller
├── entity
├── exception
├── repository
└── service

src/main/resources
└── static
    └── index.html
```

## 12. Observações

O PulsePay é um protótipo acadêmico desenvolvido para demonstrar conceitos de desenvolvimento de aplicações Java, APIs REST, persistência de dados, regras de negócio, auditoria e comunicação em tempo real.

A validação de CPF verifica sua estrutura e seus dígitos verificadores, mas não realiza consulta oficial de identidade ou titularidade.

O projeto não deve ser considerado uma plataforma financeira pronta para produção.

---

**Projeto acadêmico — Equipe PulsePay**