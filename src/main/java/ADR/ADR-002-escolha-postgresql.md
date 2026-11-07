# ADR-002 – Escolha do PostgreSQL como banco de dados do PulsePay

**Projeto:** PulsePay – Carteira Digital  
**Curso:** Desenvolvimento Full Stack e DevOps – Unifametro  
**Data:** 07/11/2026  
**Status:** Aceito  
**Responsáveis:** Equipe PulsePay  
**Categoria:** Arquitetura de Software – Persistência de Dados

## 1. Contexto

O PulsePay é um protótipo acadêmico de carteira digital que permite o cadastro de usuários e a realização de operações financeiras simuladas, como consulta de saldo, depósitos e transferências.

A aplicação necessita armazenar informações estruturadas, incluindo:

- Dados dos usuários.
- Informações das contas.
- Saldos disponíveis.
- Registros de operações.
- Dados relacionados às transferências.

Essas informações possuem relacionamentos entre si e precisam ser armazenadas de forma consistente.

Por exemplo, uma transferência envolve uma conta de origem, uma conta de destino e um valor. A persistência dessas informações exige um banco de dados que ofereça suporte a relacionamentos, integridade dos dados e transações.

Dessa forma, foi necessário selecionar um sistema gerenciador de banco de dados que atendesse às necessidades do projeto e permitisse integração com Java e Spring Boot.

## 2. Requisitos e fatores considerados

A escolha do banco de dados foi orientada pelos seguintes fatores:

- **Modelo relacional:** capacidade de organizar os dados em tabelas relacionadas.
- **Integridade dos dados:** suporte a chaves primárias, chaves estrangeiras e restrições.
- **Transações:** suporte a operações que precisam ser executadas de forma consistente.
- **Consultas:** possibilidade de consultar e relacionar informações de diferentes tabelas.
- **Integração:** compatibilidade com Java, Spring Boot e Spring Data JPA.
- **Custo:** utilização de uma tecnologia gratuita e de código aberto.
- **Evolução:** possibilidade de ampliar a estrutura de dados conforme o sistema evoluir.

## 3. Alternativas consideradas

### 3.1 PostgreSQL

O PostgreSQL é um sistema gerenciador de banco de dados relacional, gratuito e de código aberto.

Oferece suporte a relacionamentos, transações, restrições de integridade e diferentes tipos de dados. Também possui integração com aplicações Java por meio de drivers JDBC e ferramentas ORM.

### 3.2 MySQL

O MySQL é outro sistema gerenciador de banco de dados relacional amplamente utilizado em aplicações web.

Também seria uma alternativa viável para o PulsePay, pois oferece suporte a tabelas relacionadas, consultas SQL e integração com Java.

### 3.3 H2 Database

O H2 é um banco de dados que pode ser utilizado em aplicações Java, inclusive em ambientes de desenvolvimento e testes.

Apesar de ser útil para protótipos e testes automatizados, a equipe optou por utilizar PostgreSQL como banco principal do projeto.

## 4. Decisão

Foi escolhido o **PostgreSQL** como sistema gerenciador de banco de dados do PulsePay.

A aplicação utiliza o PostgreSQL para armazenar os dados de usuários, contas e operações realizadas no sistema.

A integração com o back-end é realizada por meio do ecossistema Java, Spring Boot e Spring Data JPA.

## 5. Justificativa da decisão

A escolha do PostgreSQL foi considerada adequada aos requisitos do PulsePay pelos seguintes motivos:

- É um banco de dados relacional, adequado para dados estruturados e relacionados.
- Permite representar relacionamentos entre usuários, contas e operações.
- Oferece suporte a transações, contribuindo para a consistência das operações.
- Possui recursos de integridade referencial, como chaves primárias e estrangeiras.
- É gratuito e de código aberto.
- Possui integração com Java e Spring Boot.
- Permite realizar consultas SQL para análise e visualização dos dados.
- Pode ser utilizado em projetos acadêmicos e também em aplicações de maior complexidade.

Por se tratar de um sistema que simula movimentações financeiras, a utilização de um banco relacional é apropriada para representar as relações entre as contas e seus registros de movimentação.

## 6. Consequências da decisão

### Consequências positivas

- Armazenamento estruturado das informações.
- Possibilidade de relacionar usuários, contas e operações.
- Utilização de transações para apoiar a consistência das movimentações.
- Facilidade de consulta e análise dos dados por meio de SQL.
- Integração com Spring Data JPA.
- Utilização de uma tecnologia gratuita e amplamente documentada.

### Consequências e limitações

- É necessário instalar e configurar o PostgreSQL no ambiente de desenvolvimento.
- A aplicação depende da disponibilidade do banco de dados.
- É necessário gerenciar credenciais e configurações de conexão.
- A estrutura das tabelas e os relacionamentos precisam ser definidos adequadamente.
- A utilização do PostgreSQL não garante automaticamente a consistência de todas as regras financeiras. A aplicação também precisa implementar corretamente as transações e regras de negócio.
- Para utilização em um ambiente real, seriam necessários mecanismos adicionais de backup, monitoramento, controle de acesso e proteção dos dados.

## 7. Impacto na arquitetura

A escolha do PostgreSQL influenciou a estrutura de persistência do PulsePay.

As entidades da aplicação representam os dados armazenados, enquanto os repositórios são responsáveis por realizar operações de acesso ao banco.

O Spring Data JPA facilita a integração entre as entidades Java e as tabelas do PostgreSQL, reduzindo a necessidade de escrever manualmente consultas SQL para operações comuns.

A utilização do banco relacional permite realizar consultas que relacionam informações de usuários, contas e operações, contribuindo para funcionalidades como consulta de saldo e histórico de movimentações.

## 8. Considerações sobre segurança e consistência

Como o PulsePay simula operações financeiras, a persistência dos dados deve ser tratada com atenção.

O PostgreSQL oferece recursos importantes para apoiar a consistência dos dados, mas a segurança e a integridade do sistema dependem também da implementação da aplicação.

Entre os pontos que devem ser considerados em futuras evoluções estão:

- Garantir que cada usuário acesse somente as contas autorizadas.
- Validar os valores de depósitos e transferências.
- Garantir que uma transferência seja registrada de forma consistente.
- Evitar alterações indevidas nos saldos.
- Implementar testes automatizados para as operações.
- Realizar backups e monitorar o banco de dados.

Esses pontos são especialmente importantes caso o protótipo venha a ser ampliado.

## 9. Conclusão

A escolha do PostgreSQL foi considerada adequada para o PulsePay por oferecer um modelo relacional, suporte a transações, integridade dos dados e integração com Java e Spring Boot.

A tecnologia permitiu armazenar e consultar as informações do sistema de forma estruturada, atendendo às necessidades do protótipo acadêmico.

A decisão contribuiu para a implementação das funcionalidades de cadastro, consulta de contas, depósitos e transferências, além de permitir futuras evoluções na arquitetura de persistência.

O PostgreSQL foi escolhido para atender aos requisitos do projeto acadêmico. Sua utilização, isoladamente, não torna o PulsePay apto a operar como uma instituição financeira real.
