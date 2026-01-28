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
| JUnit 5 | - | Testes unitarios |
| Docker | - | Containerizacao |

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

### Relatorio HTML

```
build/reports/tests/test/index.html
```

### Cobertura de Testes

| Categoria | Testes | Descricao |
|-----------|--------|-----------|
| Arquitetura | 32 | ArchUnit (camadas, nomenclatura, sufixos) |
| Dominio | 12 | Customer e Address |
| Use Cases | 15 | CRUD use cases |
| Adapters | 5 | Repository adapters |
| Controller | 8 | REST API com MockMvc |
| Aplicacao | 1 | Context loads |
| **Total** | **73** | |

### Testes de Arquitetura

O projeto utiliza ArchUnit para garantir:

- **Regras de camadas**: Adapters nao acessam outros adapters diretamente
- **Convencoes de nomenclatura**: Classes com sufixos corretos nos pacotes corretos
- **Sufixos obrigatorios**: Ex: classes em `usecase/` devem terminar com `UseCase`

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
{"id":"<customer-id>","name":"Joao","cpf":"12345678900","isValidCpf":true}
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

O JAR sera gerado em `build/libs/hexagonal-0.0.1-SNAPSHOT.jar`

### Executar JAR

```bash
java -jar build/libs/hexagonal-0.0.1-SNAPSHOT.jar
```

## Vantagens da Arquitetura Hexagonal

1. **Organizacao**: Estrutura clara e previsivel
2. **Flexibilidade**: Facil trocar tecnologias (ex: MongoDB por PostgreSQL)
3. **Testabilidade**: Core isolado, facil de testar
4. **Principios SOLID**: SRP e ISP aplicados naturalmente
5. **Manutencao**: Mudancas isoladas por camada

## Desvantagens

1. **Quantidade de codigo**: Mais classes e interfaces que MVC tradicional
2. **Curva de aprendizado**: Requer entendimento dos conceitos

## Licenca

Este projeto foi desenvolvido para fins educacionais.
