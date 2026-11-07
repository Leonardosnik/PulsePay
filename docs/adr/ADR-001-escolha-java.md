# ADR-001 – Escolha da linguagem Java para o desenvolvimento do PulsePay

**Projeto:** PulsePay – Carteira Digital  
**Curso:** Desenvolvimento Full Stack e DevOps – Unifametro  
**Data:** 07/11/2026  
**Status:** Aceito  
**Responsáveis:** Equipe PulsePay  
**Categoria:** Arquitetura de Software – Tecnologia de Desenvolvimento

## 1. Contexto

O PulsePay é um protótipo acadêmico de carteira digital desenvolvido com o objetivo de aplicar conhecimentos de desenvolvimento de software, integração entre sistemas e persistência de dados.

A aplicação possui funcionalidades como cadastro de usuários, autenticação, consulta de saldo, depósitos e transferências entre contas.

Para atender a esses requisitos, foi necessário selecionar uma linguagem de programação para o desenvolvimento do back-end. A tecnologia escolhida deveria permitir a organização do código, implementação das regras de negócio, integração com o banco de dados e utilização de recursos de segurança.

Além disso, a linguagem deveria possuir um ecossistema de ferramentas que facilitasse o desenvolvimento e permitisse futuras melhorias no sistema.

## 2. Requisitos e fatores considerados

A escolha da linguagem foi orientada pelos seguintes fatores:

- **Organização do código:** possibilidade de estruturar a aplicação em camadas e separar responsabilidades.
- **Integração com banco de dados:** suporte à persistência de informações e ao uso de ferramentas ORM.
- **Desenvolvimento web:** disponibilidade de frameworks para criação de aplicações e APIs.
- **Segurança:** recursos e bibliotecas que auxiliem na implementação de autenticação, validação e controle de acesso.
- **Manutenibilidade:** facilidade para compreender, corrigir e evoluir o código.
- **Ecossistema:** disponibilidade de ferramentas, documentação e bibliotecas.
- **Objetivo acadêmico:** aplicação prática de conceitos de programação orientada a objetos e desenvolvimento back-end.

## 3. Alternativas consideradas

### 3.1 Java

Java é uma linguagem orientada a objetos, amplamente utilizada no desenvolvimento de aplicações corporativas. Possui um ecossistema consolidado e integração com frameworks como Spring Boot.

Sua utilização permite estruturar o sistema em classes, serviços, controladores e componentes de persistência.

### 3.2 Python

Python também permite o desenvolvimento de aplicações web e possui frameworks como Django e Flask.

É uma alternativa com sintaxe simples e boa produtividade, porém a equipe optou por utilizar Java no desenvolvimento do PulsePay, considerando o conjunto de tecnologias adotado no projeto.

### 3.3 JavaScript com Node.js

JavaScript com Node.js permite desenvolver aplicações web utilizando JavaScript no servidor e no cliente.

Apesar de ser uma alternativa viável, a equipe optou por Java e Spring Boot para a implementação do back-end do projeto.

## 4. Decisão

Foi escolhida a linguagem **Java 21**, utilizando o framework **Spring Boot**, para o desenvolvimento do back-end do PulsePay.

A aplicação foi estruturada de forma a separar as responsabilidades entre controladores, serviços, repositórios e entidades, favorecendo a organização e a manutenção do código.

## 5. Justificativa da decisão

A escolha do Java foi considerada adequada aos requisitos do PulsePay pelos seguintes motivos:

- A linguagem oferece suporte à programação orientada a objetos.
- O Spring Boot facilita a configuração e o desenvolvimento de aplicações web.
- O Spring Data JPA permite integrar a aplicação ao banco de dados por meio de entidades e repositórios.
- O ecossistema Java disponibiliza bibliotecas para validação, segurança e tratamento de requisições.
- A separação em camadas contribui para a organização das regras de negócio.
- Java 21 oferece uma versão moderna da plataforma, com suporte de longo prazo (LTS).

A combinação Java e Spring Boot permitiu desenvolver as funcionalidades principais do sistema sem a necessidade de implementar manualmente toda a infraestrutura de uma aplicação web.

## 6. Consequências da decisão

### Consequências positivas

- Organização do código em camadas.
- Facilidade de integração com o PostgreSQL.
- Utilização de bibliotecas consolidadas para desenvolvimento web.
- Possibilidade de evolução do sistema com novas funcionalidades.
- Aplicação prática de conceitos de orientação a objetos e arquitetura de software.
- Maior facilidade de manutenção quando comparada a uma implementação sem estrutura definida.

### Consequências e limitações

- A aplicação depende da instalação e configuração do ambiente Java.
- O Spring Boot e suas dependências exigem gerenciamento por meio do Maven.
- A estrutura em camadas requer atenção para evitar complexidade desnecessária.
- A utilização de Java e Spring Boot não garante, por si só, a segurança da aplicação. As regras de autenticação, autorização e validação precisam ser corretamente implementadas e testadas.

## 7. Impacto na arquitetura

A escolha do Java influenciou diretamente a arquitetura do PulsePay.

O back-end utiliza componentes organizados em camadas:

- **Controllers:** recebem e encaminham as requisições.
- **Services:** concentram as regras de negócio.
- **Repositories:** realizam operações de persistência.
- **Entities:** representam os dados armazenados no banco.

Essa estrutura contribui para separar as responsabilidades e facilita futuras alterações no sistema.

## 8. Conclusão

A escolha do Java 21 com Spring Boot foi considerada adequada para o desenvolvimento do PulsePay, pois oferece recursos para criação de aplicações web organizadas, integração com banco de dados e implementação de regras de negócio.

A decisão também contribuiu para os objetivos acadêmicos do projeto, permitindo aplicar conceitos de desenvolvimento back-end, arquitetura em camadas e integração de tecnologias.

A escolha não representa uma garantia de desempenho ou segurança em ambiente financeiro real. Para uma futura evolução do sistema, seriam necessários testes automatizados, validações de segurança, análise de desempenho e revisão das regras de negócio.
