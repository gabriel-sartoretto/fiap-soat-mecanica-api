# 🚗 FIAP SOAT - Mecânica API

API desenvolvida para gerenciamento de uma oficina mecânica, 
como parte do **Tech Challenge – Fase 1**.

---

## 📌 Problema

O sistema foi idealizado para resolver dores comuns de oficinas:

* Erros na priorização dos atendimentos
* Falhas no controle de peças e insumos
* Dificuldade em acompanhar o status dos serviços
* Perda de histórico de clientes e veículos
* Ineficiência no fluxo de orçamentos

---

## 🧱 Tecnologias

* Java 21
* Spring Boot 3
* PostgreSQL 17
* Flyway (migrations)
* Docker
* Maven

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, instale:

* Java 21
* Maven 3.9+
* Docker + Docker Compose

---

## 🚀 Como rodar o projeto

### 🔹 1. Subir o banco de dados

```bash
docker-compose up -d
```

Isso irá subir um container PostgreSQL na porta **5432**.

---

### 🔹 2. Build do projeto (opcional)

Caso seja o primeiro uso ou queira validar o build:

```bash
mvn clean install
```

---

### 🔹 3. Rodar a aplicação

```bash
mvn spring-boot:run
```

ou

```bash
./mvnw spring-boot:run
```

---

## 🔐 Autenticação

A API utiliza **JWT**.

### Fluxo:

1. Criar usuário (`/usuario`)
2. Fazer login (`/auth/login`)
3. Receber token
4. Enviar no header:
5. Cada endpoint tem o Cargo autorizado entre os presentes ('ATENDENTE', 'MECANICO', 'ALMOXARIFE')

```http
Authorization: Bearer SEU_TOKEN
```

---

## 📖 Documentação (Swagger)

Após subir a aplicação:

👉 http://localhost:8080/swagger-ui.html

---

## 📂 Estrutura do projeto

```
domain        → regras de negócio (entidades, value objects)
application   → casos de uso
adapters      → controllers, repositorios
infra         → banco (JPA), segurança
```

---

## 🧪 Rodar testes

```bash
mvn test
```

---

## 🛠️ Padrões utilizados

* Clean Architecture - Arquitetura Hexagonal
* SOLID
* Value Objects para validações (CPF, CNPJ, Email, Placa, Telefone)
* Separação Domain vs Entity (JPA)

---

## 👨‍💻 Autor

Projeto desenvolvido para o Tech Challenge FIAP.

---

## 📄 Licença

Uso educacional.