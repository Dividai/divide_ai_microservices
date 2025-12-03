# Divide AI - Sistema de Divisão de Despesas

Sistema de microserviços para gerenciamento e divisão de despesas em grupo.

##  Arquitetura

O projeto é composto por 2 microserviços independentes:

### 1. **Users Service** (Porta 8082)
- Gerenciamento de usuários e grupos
- Banco de dados: AWS RDS PostgreSQL
- Tecnologias: Spring Boot, JPA, PostgreSQL

### 2. **Despesas Service** (Porta 8083)
- Gerenciamento de despesas e cálculos de divisão
- Banco de dados: AWS DynamoDB
- Tecnologias: Spring Boot, AWS SDK, DynamoDB

##  Quick Start

### Pré-requisitos
- Docker e Docker Compose
- Java 21
- Maven 3.9+
- Credenciais AWS 
- Acesso ao AWS RDS PostgreSQL

### Configuração

1. **Clone o repositório**
```bash
git clone <repository-url>
cd DivideAI-Microservices
```

2. **Configure as credenciais**

Para cada serviço, copie o `.env.example` para `.env` e configure:

```bash
# Users Service
cd divide-users-service
cp .env.example .env
# Edite o .env com as credenciais do PostgreSQL

# Despesas Service
cd ../divide-despesas-service
cp .env.example .env
# Edite o .env com as credenciais AWS
```

3. **Inicie os serviços**

```bash
# Users Service
cd divide-users-service
docker-compose up -d

# Despesas Service
cd ../divide-despesas-service
docker-compose up -d
```

## Endpoints

### Users Service (http://localhost:8082)
- Swagger UI: http://localhost:8082/swagger-ui.html
- API Docs: http://localhost:8082/api-docs

### Despesas Service (http://localhost:8083)
- Swagger UI: http://localhost:8083/swagger-ui.html
- API Docs: http://localhost:8083/api-docs

## 📁 Estrutura do Projeto

```
DivideAI-Microservices/
├── divide-users-service/       # Microserviço de Usuários e Grupos
│   ├── src/                    # Código fonte
│   ├── postman/                # Collections Postman
│   ├── Dockerfile              # Imagem Docker
│   ├── docker-compose.yml      # Orquestração
│   └── pom.xml                 # Dependências Maven
│
└── divide-despesas-service/    # Microserviço de Despesas
    ├── src/                    # Código fonte
    ├── postman/                # Collections Postman
    ├── Dockerfile              # Imagem Docker
    ├── docker-compose.yml      # Orquestração
    └── pom.xml                 # Dependências Maven
```

##  Testes

Importe as collections do Postman localizadas em cada pasta `postman/` dos serviços.




