# Customer API - Hexagonal Architecture

API de Cliente (CRUD) implementada usando Arquitetura Hexagonal (Ports and Adapters) com Java 17 e Spring Boot 3.2.

## Tecnologias

| Tecnologia | Versao | Descricao |
|------------|--------|-----------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2 | Framework web |
| Gradle | 8.5 | Gerenciador de dependencias |
| MongoDB | 7.0 | Banco de dados NoSQL |
| Apache Kafka | Latest | Mensageria assincrona |
| OpenFeign | - | Cliente HTTP declarativo |
| MapStruct | 1.5.5 | Mapeamento de objetos |
| Lombok | - | Reducao de boilerplate |
| ArchUnit | 1.2.1 | Testes de arquitetura |
| JaCoCo | 0.8.11 | Cobertura de codigo |
| JUnit 5 | - | Testes unitarios |
| SonarQube | 9.9.8 | Analise de qualidade |
| Docker | - | Containerizacao |

## Qualidade de Codigo

O projeto utiliza SonarQube para analise continua de qualidade e JaCoCo para cobertura de testes, com configuracoes customizadas para arquitetura hexagonal.

### Metricas Atuais

| Metrica | Valor | Status |
|---------|-------|--------|
| Quality Gate | Hexagonal Clean Code | Passed |
| Coverage | 76.1% | A |
| Bugs | 0 | A |
| Vulnerabilities | 0 | A |
| Code Smells | 0 | A |
| Security Hotspots | 0 | A |
| Duplications | 0.0% | A |
| Unit Tests | 120 | - |

### Quality Gate - Hexagonal Clean Code

Quality Gate customizado para projetos com arquitetura hexagonal:

| Condicao | Threshold | Descricao |
|----------|-----------|-----------|
| Coverage | >= 70% | Cobertura minima de codigo |
| Duplicated Lines | <= 3% | Maximo de duplicacao |
| Maintainability Rating | A | Nota de manutenibilidade |
| Reliability Rating | A | Nota de confiabilidade |
| Security Rating | A | Nota de seguranca |
| Security Hotspots Reviewed | 100% | Hotspots revisados |
| Blocker Issues | 0 | Nenhum issue blocker |
| Critical Issues | 0 | Nenhum issue critico |

### Quality Profile - Hexagonal Spring Boot

Quality Profile customizado com 487 regras ativas, incluindo regras especificas para:

- **Spring Framework**: Injecao de dependencia, Controllers, Components
- **Bean Validation**: JSR 380
- **Exception Handling**: Preservacao de excecoes originais
- **Security**: Regras OWASP para aplicacoes web

### Cobertura por Camada (JaCoCo)

Configuracao de cobertura diferenciada por camada da arquitetura:

| Camada | Cobertura Minima | Justificativa |
|--------|------------------|---------------|
| Use Cases | 80% | Core da aplicacao, regras de negocio |
| Domain | 80% | Entidades e objetos de valor |
| Adapters In | 60% | Controllers e Consumers |
| Adapters Out | 60% | Repositories, Clients, Producers |
| Global | 60% | Minimo geral do projeto |

### Exclusoes Inteligentes

Classes excluidas da analise de cobertura (nao afetam metricas):

| Tipo | Padrao | Motivo |
|------|--------|--------|
| Configuracoes | `**/config/**` | Beans e setup |
| Entidades | `**/entity/**` | POJOs sem logica |
| DTOs Request | `**/request/**` | Objetos de transferencia |
| DTOs Response | `**/response/**` | Objetos de transferencia |
| Messages | `**/message/**` | DTOs Kafka |
| Mappers | `**/mapper/**` | Gerados por MapStruct |
| Exceptions | `**/exception/**` | Classes de excecao |
| Application | `**/*Application.java` | Classe main |

### Pipeline CI/CD

O projeto utiliza GitHub Actions para integracao continua:

1. **Build** - Compila o projeto
2. **Test** - Executa testes unitarios e de integracao
3. **JaCoCo** - Gera relatorio de cobertura
4. **SonarQube** - Analise estatica de codigo

## Arquitetura Hexagonal

### Conceitos

A Arquitetura Hexagonal (ou Ports and Adapters) isola a logica de negocio do mundo externo:

- **Core (Domain)**: Regras de negocio isoladas, sem dependencias externas
- **Input Ports**: Interfaces que definem como acessar o core
- **Output Ports**: Interfaces que definem como o core acessa recursos externos
- **Adapters**: Implementacoes concretas das portas

### Estrutura

```
src/main/java/com/davijaf/hexagonal/
├── HexagonalApplication.java
├── config/                          # Configuracoes e beans
├── application/
│   ├── core/
│   │   ├── domain/                  # Entidades (Customer, Address)
│   │   ├── exceptions/              # Exceptions customizadas
│   │   └── usecase/                 # Regras de negocio
│   └── ports/
│       ├── in/                      # Portas de entrada
│       └── out/                     # Portas de saida
└── adapters/
    ├── in/
    │   ├── controller/              # REST API
    │   │   ├── handler/             # Exception handlers
    │   │   ├── mapper/              # Request/Response mappers
    │   │   ├── request/             # DTOs de entrada
    │   │   └── response/            # DTOs de saida
    │   └── consumer/                # Kafka consumers
    └── out/
        ├── repository/              # MongoDB
        │   ├── entity/              # Entidades JPA
        │   └── mapper/              # Entity mappers
        ├── client/                  # Feign clients
        └── producer/                # Kafka producers
```

### Camadas

| Camada | Pacote | Descricao |
|--------|--------|-----------|
| Domain | `application.core.domain` | Entidades e objetos de valor |
| Exceptions | `application.core.exceptions` | Exceptions do core |
| Use Cases | `application.core.usecase` | Regras de negocio |
| Input Ports | `application.ports.in` | Interfaces de entrada |
| Output Ports | `application.ports.out` | Interfaces de saida |
| Controller | `adapters.in.controller` | REST API |
| Handler | `adapters.in.controller.handler` | Exception handlers |
| Consumer | `adapters.in.consumer` | Kafka consumers |
| Repository | `adapters.out.repository` | Persistencia MongoDB |
| Client | `adapters.out.client` | Cliente HTTP externo |
| Producer | `adapters.out.producer` | Kafka producers |

## Fluxo da Aplicacao

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Client    │────>│  Controller │────>│   UseCase   │
│  (REST)     │     │  (Adapter)  │     │   (Core)    │
└─────────────┘     └─────────────┘     └──────┬──────┘
                                               │
                    ┌──────────────────────────┼──────────────────────────┐
                    │                          │                          │
                    v                          v                          v
            ┌───────────────┐         ┌───────────────┐         ┌───────────────┐
            │   MongoDB     │         │  Feign Client │         │    Kafka      │
            │  (Repository) │         │   (Address)   │         │  (Producer)   │
            └───────────────┘         └───────────────┘         └───────────────┘
```

1. Receber requisicao de cadastro de cliente (REST)
2. Buscar endereco via CEP (API externa via WireMock)
3. Salvar cliente no MongoDB
4. Publicar CPF na fila Kafka para validacao
5. Consumir resultado da validacao de outra fila Kafka
6. Atualizar status do cliente no MongoDB

## Quick Start

### Pre-requisitos

- Java 17+
- Docker e Docker Compose
- Gradle 8.5+ (ou use o wrapper)

### 1. Subir infraestrutura

```bash
docker-compose up -d
```

Isso inicia:
- MongoDB (porta 27017)
- Mongo Express (porta 8081)
- Zookeeper (porta 2181)
- Kafka (porta 9092)

### 2. Subir WireMock (mock da API de endereco)

```bash
# Baixar WireMock (se necessario)
curl -o wiremock.jar https://repo1.maven.org/maven2/org/wiremock/wiremock-standalone/3.3.1/wiremock-standalone-3.3.1.jar

# Executar
java -jar wiremock.jar --port 8082 --root-dir wiremock
```

### 3. Executar a aplicacao

```bash
./gradlew bootRun
```

A API estara disponivel em `http://localhost:8080`

## API Endpoints

| Metodo | Endpoint | Descricao | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/customers` | Criar cliente | 200 |
| GET | `/api/v1/customers/{id}` | Buscar cliente | 200 / 404 |
| PUT | `/api/v1/customers/{id}` | Atualizar cliente | 204 / 404 |
| DELETE | `/api/v1/customers/{id}` | Remover cliente | 204 / 404 |

### Exemplos

#### Criar Cliente

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Joao Silva",
    "cpf": "12345678900",
    "zipCode": "38400000"
  }'
```

#### Buscar Cliente

```bash
curl http://localhost:8080/api/v1/customers/{id}
```

Resposta:
```json
{
  "name": "Joao Silva",
  "cpf": "12345678900",
  "address": {
    "street": "Rua Hexagonal",
    "city": "Uberlandia",
    "state": "Minas Gerais"
  },
  "isValidCpf": false
}
```

#### Erro 404

```json
{
  "timestamp": 1706396000000,
  "status": 404,
  "message": "Object with id 123 not found",
  "path": "/api/v1/customers/123"
}
```

### CEPs Validos (WireMock)

| CEP | Cidade | Estado |
|-----|--------|--------|
| 38400000 | Uberlandia | Minas Gerais |
| 38400001 | Sao Paulo | Sao Paulo |

## Postman Collection

Importe a collection para testar a API:

1. Abra o Postman
2. Click **Import**
3. Selecione:
   - `postman/Customer-API.postman_collection.json`
   - `postman/Local.postman_environment.json`

### Conteudo

| Pasta | Requests | Descricao |
|-------|----------|-----------|
| Customers | 4 | CRUD completo |
| Validation Tests | 4 | Testes de validacao |
| Integration Flow | 6 | Fluxo completo |

## Testes

### Executar todos os testes

```bash
./gradlew test
```

### Executar com cobertura

```bash
./gradlew test jacocoTestReport
```

### Relatorios

| Relatorio | Caminho |
|-----------|---------|
| Testes | `build/reports/tests/test/index.html` |
| Cobertura | `build/reports/jacoco/test/html/index.html` |

### Estrutura de Testes

```
src/test/java/com/davijaf/hexagonal/
├── HexagonalApplicationTests.java
├── adapters/
│   ├── in/
│   │   ├── consumer/
│   │   │   ├── ReceiveValidatedCpfConsumerTest.java
│   │   │   └── message/
│   │   │       └── CustomerMessageTest.java
│   │   └── controller/
│   │       └── CustomerControllerTest.java
│   └── out/
│       ├── client/
│       │   ├── FindAddressByZipCodeAdapterTest.java
│       │   └── response/
│       │       └── AddressResponseTest.java
│       ├── producer/
│       │   └── SendCpfForValidationAdapterTest.java
│       └── repository/
│           ├── DeleteCustomerByIdAdapterTest.java
│           ├── FindCustomerByIdAdapterTest.java
│           ├── InsertCustomerAdapterTest.java
│           ├── UpdateCustomerAdapterTest.java
│           └── entity/
│               ├── AddressEntityTest.java
│               └── CustomerEntityTest.java
├── application/
│   └── core/
│       ├── domain/
│       │   ├── AddressTest.java
│       │   └── CustomerTest.java
│       └── usecase/
│           ├── DeleteCustomerByIdUseCaseTest.java
│           ├── FindCustomerByIdUseCaseTest.java
│           ├── InsertCustomerUseCaseTest.java
│           └── UpdateCustomerUseCaseTest.java
└── architecture/
    ├── DomainComponentsTest.java
    ├── GeneralCodingRulesTest.java
    ├── LayerArchitectureTest.java
    ├── NamingConventionTest.java
    ├── SlicesTest.java
    └── SpringCodingRulesTest.java
```

### Cobertura de Testes

| Categoria | Arquivos | Descricao |
|-----------|----------|-----------|
| Arquitetura | 6 | ArchUnit (camadas, nomenclatura, slices, regras) |
| Dominio | 2 | Customer e Address |
| Use Cases | 4 | CRUD use cases |
| Adapters In | 3 | Controller, Consumer, DTOs |
| Adapters Out | 8 | Repository, Client, Producer, Entities |
| Aplicacao | 1 | Context loads |

### Testes de Arquitetura (ArchUnit)

O projeto utiliza ArchUnit para garantir a integridade da arquitetura hexagonal:

| Arquivo | Descricao |
|---------|-----------|
| `LayerArchitectureTest` | Valida dependencias entre camadas |
| `DomainComponentsTest` | Garante que domain nao depende de adapters/frameworks |
| `SlicesTest` | Verifica ausencia de ciclos e independencia de adapters |
| `GeneralCodingRulesTest` | Regras gerais (sem System.out, excecoes genericas, etc) |
| `NamingConventionTest` | Convencoes de nomenclatura |
| `SpringCodingRulesTest` | Boas praticas Spring (desabilitado como TODO) |

#### Regras Validadas

- **Camadas**: Adapters nao acessam outros adapters diretamente
- **Domain**: Nao depende de Spring, adapters ou config
- **Use Cases**: Nao dependem de adapters
- **Ports**: Nao dependem de adapters
- **Slices**: Sem dependencias ciclicas
- **Convencoes**: Classes com sufixos corretos nos pacotes corretos

## Analise SonarQube

### Executar analise local

```bash
./gradlew test jacocoTestReport sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=SEU_TOKEN
```

### Verificar cobertura local

```bash
# Executar testes com verificacao de cobertura
./gradlew check

# Gerar apenas relatorio de cobertura
./gradlew test jacocoTestReport
```

O comando `check` executa `jacocoTestCoverageVerification` que valida as regras de cobertura por camada.

## Configuracao

### application.yml

```yaml
server:
  port: 8080

spring:
  data:
    mongodb:
      uri: mongodb://root:example@localhost:27017/hexagonal?authSource=admin

kafka:
  bootstrap-servers: localhost:9092

davijaf:
  client:
    address:
      url: http://localhost:8082/addresses
```

### build.gradle - SonarQube e JaCoCo

```groovy
plugins {
    id 'jacoco'
    id "org.sonarqube" version "5.0.0.4638"
}

sonar {
    properties {
        // Identificacao do projeto
        property "sonar.projectKey", "seu-project-key"
        property "sonar.projectName", "Hexagonal Treinamento"

        // Cobertura JaCoCo
        property "sonar.coverage.jacoco.xmlReportPaths",
                 "${buildDir}/reports/jacoco/test/jacocoTestReport.xml"

        // Exclusoes de cobertura (DTOs, configs, mappers)
        property "sonar.coverage.exclusions", [
            "**/config/**", "**/entity/**", "**/request/**",
            "**/response/**", "**/message/**", "**/mapper/**",
            "**/exception/**", "**/*Application.java"
        ].join(",")

        // Exclusoes de duplicacao
        property "sonar.cpd.exclusions", [
            "**/entity/**", "**/request/**",
            "**/response/**", "**/message/**"
        ].join(",")

        // Configuracoes Java
        property "sonar.java.source", "17"
        property "sonar.sourceEncoding", "UTF-8"
    }
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.named('test') {
    useJUnitPlatform()
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}

// Verificacao de cobertura por camada
jacocoTestCoverageVerification {
    violationRules {
        // Regra global
        rule {
            limit { counter = 'LINE'; minimum = 0.60 }
        }
        // Use Cases - 80%
        rule {
            element = 'CLASS'
            includes = ['com.davijaf.hexagonal.application.core.usecase.*']
            limit { counter = 'LINE'; minimum = 0.80 }
        }
        // Domain - 80%
        rule {
            element = 'CLASS'
            includes = ['com.davijaf.hexagonal.application.core.domain.*']
            limit { counter = 'LINE'; minimum = 0.80 }
        }
        // Adapters - 60%
        rule {
            element = 'CLASS'
            includes = ['com.davijaf.hexagonal.adapters.**']
            excludes = ['*Mapper*', '*Entity*', '*Response*', '*Request*']
            limit { counter = 'LINE'; minimum = 0.60 }
        }
    }
}

tasks.named('check') {
    dependsOn jacocoTestCoverageVerification
}
```

### Docker Compose

| Servico | Porta | Credenciais |
|---------|-------|-------------|
| MongoDB | 27017 | root / example |
| Mongo Express | 8081 | admin / pass |
| Kafka | 9092 | - |
| Zookeeper | 2181 | - |

## Kafka Topics

| Topico | Descricao |
|--------|-----------|
| `tp-cpf-validation` | Envio de CPF para validacao |
| `tp-cpf-validated` | Recebimento do resultado da validacao |

### Simular validacao de CPF

```bash
# Enviar mensagem para o topico de validacao
docker exec -it hexagonal-kafka-1 kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic tp-cpf-validated

# Digite a mensagem JSON:
{"id":"<customer-id>","name":"Joao","cpf":"12345678900","isValidCpf":true,"zipCode":"38400000"}
```

## Build

### Compilar

```bash
./gradlew build
```

### Gerar JAR

```bash
./gradlew bootJar
```

O JAR sera gerado em `build/libs/hexagonal-1.0.0.jar`

### Executar JAR

```bash
java -jar build/libs/hexagonal-1.0.0.jar
```

## Vantagens da Arquitetura Hexagonal

1. **Organizacao**: Estrutura clara e previsivel
2. **Flexibilidade**: Facil trocar tecnologias (ex: MongoDB por PostgreSQL)
3. **Testabilidade**: Core isolado, facil de testar
4. **Principios SOLID**: SRP e ISP aplicados naturalmente
5. **Manutencao**: Mudancas isoladas por camada
6. **Qualidade**: Testes de arquitetura garantem integridade

## Desvantagens

1. **Quantidade de codigo**: Mais classes e interfaces que MVC tradicional
2. **Curva de aprendizado**: Requer entendimento dos conceitos

### Padrao de Commits

Utilizamos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nova funcionalidade
- `fix:` Correcao de bug
- `docs:` Documentacao
- `test:` Testes
- `refactor:` Refatoracao

## Licenca

Este projeto foi desenvolvido para fins educacionais.
