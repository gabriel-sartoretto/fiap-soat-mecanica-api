# FIAP SOAT - Mecanica do Braia

API REST desenvolvida para gerenciamento de uma oficina mecanica, como parte do
**Tech Challenge - Fase 1**.

## Problema

O sistema foi idealizado para resolver dores comuns de oficinas mecanicas:

- Erros na priorizacao dos atendimentos
- Falhas no controle de pecas e insumos
- Dificuldade em acompanhar o status dos servicos
- Perda de historico de clientes e veiculos
- Ineficiencia no fluxo de orcamentos e ordens de servico

## Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL 17
- Flyway
- Maven
- Docker e Docker Compose
- Kubernetes
- Spring Boot Actuator
- Swagger/OpenAPI
- JUnit, Mockito, Testcontainers e MockMvc
- JaCoCo
- SonarQube
- ZAP by Checkmarx
- GitHub Actions

## Arquitetura

O projeto segue uma organizacao inspirada em Clean Architecture e Arquitetura Hexagonal.

```text
src/main/java/br/com/fiap/soat/mecanica
|-- domain                  -> regras de negocio, entidades, enums, value objects e portas
|-- application             -> casos de uso da aplicacao
|-- adapters
|   |-- in/web              -> controllers, DTOs, mappers e filtro de seguranca
|   `-- out
|       |-- persistence     -> entidades JPA, repositories e mappers de persistencia
|       `-- security        -> servicos de JWT e criptografia de senha
`-- config                  -> seguranca, OpenAPI e tratamento global de excecoes
```

## Principais dominios

- **Usuario:** representa os usuarios autenticados da API.
- **Cliente:** pessoa fisica ou juridica atendida pela oficina.
- **Veiculo:** veiculo associado a um cliente.
- **Servico:** tipo de servico executado pela oficina.
- **Peca:** item de estoque utilizado nos servicos.
- **Ordem de Servico:** entidade central do fluxo de atendimento.
- **Prestacao de Servico:** servico incluido em uma ordem de servico.
- **Alocacao de Peca:** reserva/uso de uma peca em uma prestacao de servico.

## Fluxo da Ordem de Servico

A Ordem de Servico controla o ciclo de vida do atendimento:

```text
RECEBIDA
EM_DIAGNOSTICO
AGUARDANDO_APROVACAO
EM_EXECUCAO
FINALIZADA
ENTREGUE
```

Fluxo principal:

1. O mecanico cria uma Ordem de Servico.
2. O mecanico adiciona uma Prestacao de Servico.
3. A OS sai de `RECEBIDA` para `EM_DIAGNOSTICO`.
4. O almoxarife aloca pecas na prestacao.
5. O sistema baixa estoque e soma o valor das pecas no total da OS.
6. O mecanico envia a OS para aprovacao.
7. O mecanico inicia a execucao.
8. Ao finalizar todas as prestacoes, a OS passa para `FINALIZADA`.
9. Ao pagar, a OS passa para `ENTREGUE`.

## Perfis de acesso

A API utiliza JWT e autorizacao por cargo.

| Cargo | Responsabilidades principais |
| --- | --- |
| `ATENDENTE` | Cadastro e consulta de clientes e veiculos |
| `MECANICO` | Cadastro de servicos, ordens de servico, prestacoes e fluxo da OS |
| `ALMOXARIFE` | Cadastro de pecas e alocacao de pecas em prestacoes |

Rotas publicas:

- `POST /usuarios`
- `POST /auth/login`
- `GET /ordem-servicos/veiculo/placa/{placa}`
- Swagger/OpenAPI

As demais rotas exigem token JWT.

## Pre-requisitos

Antes de rodar o projeto, instale:

- Java 21
- Maven 3.9+ ou use o Maven Wrapper do projeto
- Docker
- Docker Compose

## Como rodar

Existem duas formas de executar o projeto. Use apenas uma delas por vez:

- **Aplicacao local + banco no Docker:** suba somente o PostgreSQL pelo Docker e
  rode a API pela IDE ou pelo Maven.
- **Tudo via Docker Compose:** suba a API e os demais servicos pelo Docker.

> Atencao: o comando `docker-compose up -d` tambem sobe a API no container
> `mecanica-api` usando a porta `8080`. Se a API ja estiver rodando pelo Docker,
> nao execute a aplicacao tambem pela IDE/Maven na mesma porta, pois ocorrera o
> erro `Port 8080 was already in use`.

### 1. Subir somente o banco PostgreSQL

Use este modo quando quiser rodar a aplicacao localmente pela IDE ou pelo Maven.

```bash
docker-compose up -d postgres
```

O PostgreSQL fica disponivel em:

```text
localhost:5433
```

Configuracao local:

```text
database: mecanica
username: postgres
password: 1234567
```

### 2. Rodar a aplicacao localmente

No Windows:

```bash
./mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

A API ficara disponivel em:

```text
http://localhost:8080
```

### 3. Subir tudo via Docker Compose

Este comando sobe a API, o PostgreSQL, o SonarQube e o banco do SonarQube:

```bash
docker-compose up -d
```

Servicos principais:

```text
API:        http://localhost:8080
PostgreSQL: localhost:5433
SonarQube: http://localhost:9000
```

Neste modo, a API ja fica disponivel em `http://localhost:8080` pelo container
`mecanica-api`. Nao e necessario iniciar a aplicacao pela IDE/Maven.

### 4. Subir somente o SonarQube

Use este modo quando a aplicacao ja estiver rodando localmente pela IDE/Maven e
voce quiser subir apenas o SonarQube e o banco dele:

```bash
docker-compose up -d sonarqube
```

O SonarQube fica disponivel em:

```text
http://localhost:9000
```

Na primeira execucao, aguarde alguns instantes ate o servico finalizar a
inicializacao. O login inicial padrao e:

```text
usuario: admin
senha: admin
```

Para verificar o status do SonarQube:

```text
http://localhost:9000/api/system/status
```

Quando o retorno indicar `status: UP`, a interface ja pode ser acessada.

## Kubernetes

Alem do Docker Compose, o projeto tambem pode ser executado em um cluster
Kubernetes local. Os manifestos ficam em:

```text
k8s/
```

### Componentes

| Arquivo | Recurso | Descricao |
| --- | --- | --- |
| `postgres-secret.yaml` | Secret | Credenciais do banco (`POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`) |
| `postgres-pvc.yaml` | PersistentVolumeClaim | Armazenamento persistente dos dados do Postgres |
| `postgres-deployment.yaml` | Deployment | Sobe o container do PostgreSQL, montando o PVC |
| `postgres-service.yaml` | Service (ClusterIP) | Expõe o Postgres dentro do cluster no host `mecanica-db` |
| `api-configmap.yaml` | ConfigMap | Configuracao nao sensivel da API (URL JDBC) |
| `api-secret.yaml` | Secret | Credenciais sensiveis da API (usuario/senha do banco, `JWT_SECRET`) |
| `api-deployment.yaml` | Deployment | Sobe os Pods da API, com probes de liveness/readiness |
| `api-service.yaml` | Service (LoadBalancer) | Expõe a API fora do cluster, em `localhost:8080` |
| `api-hpa.yaml` | HorizontalPodAutoscaler | Escala a API entre 2 e 5 réplicas com base em CPU/memória |

### Pre-requisitos

- Docker Desktop com Kubernetes habilitado (`Settings > Kubernetes > Enable Kubernetes`)
- `kubectl` (instalado automaticamente junto com o Docker Desktop)

### Como rodar

1. Build da imagem usada pelos manifestos:

```bash
docker build -t mecanica-api:local .
```

2. Subir o PostgreSQL:

```bash
kubectl apply -f k8s/postgres-secret.yaml -f k8s/postgres-pvc.yaml -f k8s/postgres-deployment.yaml -f k8s/postgres-service.yaml
```

3. Aguardar o pod do banco ficar pronto:

```bash
kubectl get pods
```

4. Subir a API:

```bash
kubectl apply -f k8s/api-configmap.yaml -f k8s/api-secret.yaml -f k8s/api-deployment.yaml -f k8s/api-service.yaml -f k8s/api-hpa.yaml
```

5. Instalar o `metrics-server` (necessario para o HPA calcular uso de CPU/memória):

```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

> Em cluster local (Docker Desktop, minikube, kind), o `metrics-server` nao
> confia por padrao no certificado do kubelet. Baixe o `components.yaml`,
> adicione o argumento `--kubelet-insecure-tls` na lista de `args` do
> Deployment `metrics-server`, e aplique o arquivo local em vez da URL.

### Acessando a API

Com o Service da API como `type: LoadBalancer`, o Docker Desktop expõe a
porta diretamente em:

```text
http://localhost:8080
```

### Verificando o cluster

```bash
kubectl get pods
kubectl get svc
kubectl get hpa
```

O health check usado pelas probes (Spring Boot Actuator) tambem pode ser
consultado diretamente:

```text
http://localhost:8080/actuator/health/readiness
```

### Sobre os Secrets versionados

Os arquivos `postgres-secret.yaml` e `api-secret.yaml` estao versionados no
repositorio com credenciais de desenvolvimento, para que qualquer pessoa
consiga clonar o projeto e subir o ambiente sem passos extras. Em um cenario
de producao, esses valores nao seriam commitados — seriam criados via
`kubectl create secret` ou um gerenciador de segredos externo (Vault, AWS
Secrets Manager, etc.), nunca versionados em texto no repositorio.

## Swagger

Com a aplicacao rodando, acesse:

```text
http://localhost:8080/swagger-ui.html
```

O Swagger esta configurado com autenticacao Bearer JWT.

## Autenticacao

### Criar usuario

```http
POST /usuarios
Content-Type: application/json
```

Exemplo:

```json
{
  "nome": "Mecanico Teste",
  "email": "mecanico@email.com",
  "senha": "Senha@123",
  "cargoEnum": "MECANICO"
}
```

### Fazer login

```http
POST /auth/login
Content-Type: application/json
```

Exemplo:

```json
{
  "email": "mecanico@email.com",
  "senha": "Senha@123"
}
```

### Usar o token

Envie o token retornado no header:

```http
Authorization: Bearer SEU_TOKEN
```

## Usuarios mockados

A migration `V9__mock_dados.sql` cria usuarios para facilitar testes manuais.

| Cargo | Email | Senha |
| --- | --- | --- |
| `ATENDENTE` | `atendente@mecanica.test` | `Senha@123` |
| `MECANICO` | `mecanico@mecanica.test` | `Senha@123` |
| `ALMOXARIFE` | `almoxarife@mecanica.test` | `Senha@123` |

## Banco de dados

O banco e versionado com Flyway em:

```text
src/main/resources/db/migration
```

Principais tabelas:

- `usuarios`
- `clientes`
- `veiculos`
- `pecas`
- `servicos`
- `ordem_servicos`
- `prestacao_servicos`
- `alocacao_pecas`

## Melhorias de Infraestrutura e Testes

- **Dockerfile multi-stage:** a imagem agora compila a aplicação em um stage com Maven e JDK e copia apenas o `.jar` gerado para uma imagem runtime menor com JRE. O build Docker e autocontido e não depende de um artefato gerado localmente.
- **Usuario nao-root:** o stage final cria usuário e grupo dedicados para a aplicação, copia o artefato com permissão adequada e executa o processo Java com `USER mecanica`.
- **Healthcheck do banco:** o PostgreSQL no Docker Compose usa `pg_isready` para indicar quando o banco está pronto para conexões.
- **Ordem de subida no Compose:** a aplicação depende do PostgreSQL com `condition: service_healthy`, evitando tentativas de conexão antes do banco estar saudável.
- **Testcontainers nos testes de integracao:** os testes JPA e de contexto sobem PostgreSQL real com Testcontainers e injetam dinâmicamente `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` e `spring.datasource.driver-class-name`.
- **Banco real nos testes:** Flyway, Hibernate/JPA e repositories são validados contra PostgreSQL, mantendo os testes unitários independentes de container.

## Testes

Rodar testes:

```bash
./mvnw.cmd test
```

No Linux/macOS:

```bash
./mvnw test
```

> Os testes de integração usam Testcontainers. Mantenha o Docker em execução antes de rodar a suíte completa.

Rodar verificação completa com JaCoCo:

```bash
./mvnw.cmd clean verify
```

O projeto possui testes para:

- Entidades de dominio
- Value Objects
- Casos de uso
- Controllers
- Mappers
- Repositories
- Seguranca/JWT
- Tratamento global de excecoes

O relatorio de cobertura fica em:

```text
target/site/jacoco/index.html
```

Atualmente, o projeto exige cobertura minima de **80%** no `mvn verify`.

## Smoke test da API

Existe um script de smoke test em:

```text
tests/api-smoke/api-smoke.mjs
```

Com a aplicacao rodando:

```bash
node .\tests\api-smoke\api-smoke.mjs
```

Tambem e possivel apontar para outra URL:

```bash
API_BASE_URL=http://localhost:8080 node tests/api-smoke/api-smoke.mjs
```

## Analise de seguranca com ZAP by Checkmarx

O projeto pode ser analisado com o ZAP by Checkmarx instalado localmente para
verificacao dinamica de vulnerabilidades na API em execucao.

Com a aplicacao rodando, configure o alvo no aplicativo:

```text
http://localhost:8080
```

Porém para que seja possível acessar Endpoints autorizados, é nesserario acessar com um token JWT no header.
A maioria dos Endpoint possui um PreAuthorize especificando qual Cargo tem acesso.

## CI

O projeto possui pipeline no GitHub Actions em:

```text
.github/workflows/ci.yml
```

A pipeline executa:

- Checkout do repositorio
- Setup do JDK 21
- `./mvnw clean verify`
- Analise SonarQube quando `SONAR_TOKEN` e `SONAR_HOST_URL` estiverem configurados

## Padroes utilizados

- Clean Architecture
- Arquitetura Hexagonal
- SOLID
- Separacao entre dominio e persistencia JPA
- Value Objects para validacoes de CPF, CNPJ, Email, Placa, Senha e Telefone
- Repositories como portas do dominio
- Use cases para orquestracao das regras de aplicacao
- DTOs e mappers nos adapters de entrada e saida
- Tratamento global de excecoes
