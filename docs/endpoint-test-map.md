# Endpoint Test Map - FIAP SOAT Mecânica API

## 🔄 Atualização após sincronização com main

Data da análise: 2026-05-02.

Resumo das mudanças detectadas no código atual:

- `POST /usuarios` agora está público em `SecurityConfig` por `requestMatchers(HttpMethod.POST, "/usuarios").permitAll()`. ✔ RESOLVIDO.
- Endpoints de peças foram corrigidos para `hasRole('ALMOXARIFE')`. ✔ RESOLVIDO.
- `VeiculoMapper.toEntity` agora seta `clienteId`. ✔ RESOLVIDO.
- `ClienteMapper.toEntity` ainda não preserva `id` nem `status`. ❌ AINDA PROBLEMA.
- `jwt.secret` continua hardcoded em `src/main/resources/application.properties` e `JWT_SECRET` continua hardcoded no `docker-compose.yml`. ❌ AINDA PROBLEMA.
- `System.out.println` foi removido de `JwtAuthenticationFilter`. ✔ RESOLVIDO.
- `DataIntegrityViolationException` agora usa `ErrorResponse`, mas ainda concatena `ex.getCause().getMessage()` na resposta. ⚠️ PARCIALMENTE RESOLVIDO.
- `V5`, `V7` e `V8` agora criam tabelas reais para `servicos`, `prestacao_servicos` e `alocacao_pecas`. ✔ RESOLVIDO.
- Foram adicionados endpoints para serviços, prestação de serviços, alocação de peças, busca de OS por veículo e cálculo de tempo médio.
- `CadastrarOrdemServicoUseCase` passou a usar `CurrentUserProvider`; o `usuarioId` saiu do payload da OS. ✔ RESOLVIDO para o risco de payload arbitrário.

Principais correções feitas pela equipe:

- Correção do path público de cadastro de usuários.
- Correção da role de almoxarife nos endpoints de peças.
- Remoção de logs diretos no filtro JWT.
- Introdução de `ErrorResponse` padronizado para erros.
- Criação do fluxo inicial de `servicos`, `prestacao_servicos` e `alocacao_pecas`.
- Uso do usuário autenticado para cadastro de cliente e ordem de serviço.

## 1. Visão geral

Documento gerado por analise estatica do backend atual para orientar testes manuais via Swagger/Postman e preparar a futura migration `src/main/resources/db/migration/V9__mock_dados.sql`.

Escopo analisado:

- Migrations Flyway em `src/main/resources/db/migration`.
- Entidades JPA, repositories e mappers em `src/main/java/br/com/fiap/soat/mecanica/adapters/out/persistence`.
- Dominio em `src/main/java/br/com/fiap/soat/mecanica/domain`.
- Use cases em `src/main/java/br/com/fiap/soat/mecanica/application`.
- Controllers, DTOs e seguranca web em `src/main/java/br/com/fiap/soat/mecanica/adapters/in/web`.
- Configuracoes `SecurityConfig`, `OpenApiConfig`, `GlobalExceptionHandler`, `JwtService`, `PasswordEncoderAdapter`.

AUSENTE: endpoints para servicos, prestacao de servicos, alocacao de pecas, estoque/movimentacao separada e transicoes de status de ordem de servico, apesar de haver metodos de dominio para algumas transicoes em `OrdemServico`.

## 2. Stack e execução local

Stack declarada:

- Java 21.
- Spring Boot `3.5.14`.
- Spring Web, Spring Data JPA, Spring Security, Bean Validation.
- PostgreSQL 17 via `docker-compose.yml`.
- Flyway.
- JWT com `io.jsonwebtoken:jjwt-* 0.12.5`.
- OpenAPI/Swagger com `springdoc-openapi-starter-webmvc-ui 2.7.0`.
- Testes com JUnit/Spring Boot Test e H2.

Configuracao local relevante:

- `src/main/resources/application.properties`: `spring.datasource.url=jdbc:postgresql://localhost:5433/mecanica`.
- `docker-compose.yml`: PostgreSQL exposto em `5433:5432`.
- `spring.jpa.hibernate.ddl-auto=validate`.
- `spring.jpa.show-sql=true`.
- `spring.flyway.enabled=true`.
- `jwt.secret` esta hardcoded no `application.properties` e tambem no `docker-compose.yml`.

INCERTEZA TECNICA: `README.md` informa porta 5432 para o banco, mas `docker-compose.yml` e `application.properties` usam porta 5433 no host.

## 3. Migrations e schema atual

Ordem efetiva esperada pelo Flyway:

1. `V1__create_usuario.sql`
2. `V2__create.cliente.sql`
3. `V3__create_veiculo.sql`
4. `V4__create_peca.sql`
5. `V5__create_servico.sql`
6. `V6__create_ordem_servico.sql`
7. `V7__create_prestacao_servico.sql`
8. `V8__create_alocacao_pecas.sql`

### `usuarios`

Criada em `V1__create_usuario.sql`.

| Coluna | Tipo | Obrigatoria | Constraints |
|---|---:|---:|---|
| `id` | `UUID` | sim | PK |
| `status` | `VARCHAR(20)` | sim | CHECK `ATIVO`, `INATIVO` |
| `nome` | `VARCHAR(255)` | sim | - |
| `email` | `VARCHAR(255)` | sim | UNIQUE |
| `senha` | `VARCHAR(255)` | sim | - |
| `cargo_enum` | `VARCHAR(30)` | sim | CHECK `MECANICO`, `ATENDENTE`, `ALMOXARIFE` |

Dados obrigatorios: pelo menos um usuario `ATENDENTE`, um `MECANICO` e um `ALMOXARIFE` para autenticar e testar autorizacao. O hash deve ser BCrypt.

### `clientes`

Criada em `V2__create.cliente.sql`.

| Coluna | Tipo | Obrigatoria | Constraints |
|---|---:|---:|---|
| `id` | `UUID` | sim | PK |
| `status` | `VARCHAR(20)` | sim | CHECK `ATIVO`, `INATIVO` |
| `nome` | `VARCHAR(255)` | sim | - |
| `cpf` | `VARCHAR(11)` | nao | UNIQUE, `uk_cliente_cpf` |
| `cnpj` | `VARCHAR(14)` | nao | UNIQUE, `uk_cliente_cnpj` |
| `email` | `VARCHAR(255)` | sim | UNIQUE, `uk_cliente_email` |
| `telefone` | `VARCHAR(20)` | nao | - |
| `usuario_id` | `UUID` | sim | indice `idx_cliente_usuario_id`, sem FK |

CHECK `chk_cliente_documento`: exatamente um entre `cpf` e `cnpj` deve estar preenchido.

Risco: `usuario_id` nao possui FK para `usuarios(id)` no banco, embora seja obrigatorio. Cliente pode referenciar usuario inexistente se inserido direto por SQL.

### `veiculos`

Criada em `V3__create_veiculo.sql`.

| Coluna | Tipo | Obrigatoria | Constraints |
|---|---:|---:|---|
| `id` | `UUID` | sim | PK |
| `status` | `VARCHAR(20)` | sim | CHECK `ATIVO`, `INATIVO` |
| `placa` | `VARCHAR(7)` | sim | UNIQUE |
| `marca` | `VARCHAR(255)` | sim | - |
| `modelo` | `VARCHAR(255)` | sim | - |
| `ano` | `VARCHAR(4)` | sim | - |
| `quantidade_eixos` | `INT` | sim | - |
| `cliente_id` | `UUID` | sim | FK `clientes(id)`, indice `idx_veiculo_cliente_id` |

Dados obrigatorios: clientes previamente existentes.

### `pecas`

Criada em `V4__create_peca.sql`.

| Coluna | Tipo | Obrigatoria | Constraints |
|---|---:|---:|---|
| `id` | `UUID` | sim | PK |
| `status` | `VARCHAR(20)` | sim | CHECK `ATIVO`, `INATIVO` |
| `nome` | `VARCHAR(255)` | sim | UNIQUE |
| `marca` | `VARCHAR(255)` | sim | - |
| `valor_unitario` | `NUMERIC(10,2)` | sim | - |
| `quantidade_estoque` | `INT` | sim | - |

Risco: banco nao impede `valor_unitario` ou `quantidade_estoque` negativos; a API valida via Bean Validation e dominio.

### `servicos`

`V5__create_servico.sql` esta vazio. AUSENTE: tabela `servicos`, entidade JPA, dominio, use cases e endpoints de servico.

Atualização após main: ✔ RESOLVIDO. `V5__create_servico.sql` agora cria `servicos` com `id UUID PRIMARY KEY`, `status VARCHAR(20) NOT NULL CHECK (status IN ('ATIVO', 'INATIVO'))`, `nome VARCHAR(100) NOT NULL UNIQUE` e `descricao TEXT`. Também existem `ServicoEntity`, domínio `Servico`, repository/use cases e `ServicoController`.

### `ordem_servicos`

Criada em `V6__create_ordem_servico.sql`.

| Coluna | Tipo | Obrigatoria | Constraints |
|---|---:|---:|---|
| `id` | `UUID` | sim | PK |
| `status` | `VARCHAR(20)` | sim | CHECK `ATIVO`, `INATIVO` |
| `situacao` | `VARCHAR(50)` | sim | sem CHECK no banco |
| `data_recebida` | `TIMESTAMP` | sim | - |
| `data_diagnostico` | `TIMESTAMP` | nao | - |
| `data_execucao` | `TIMESTAMP` | nao | - |
| `data_finalizada` | `TIMESTAMP` | nao | - |
| `data_entregue` | `TIMESTAMP` | nao | - |
| `pago` | `BOOLEAN` | nao | - |
| `valor_total` | `NUMERIC(10,2)` | nao | - |
| `observacao` | `TEXT` | nao | - |
| `veiculo_id` | `UUID` | sim | FK `veiculos(id)`, indice |
| `usuario_id` | `UUID` | sim | FK `usuarios(id)`, indice |

Dados obrigatorios: usuario mecanico e veiculo existente.

### `prestacao_servicos`

`V7__create_prestacao_servico.sql` esta vazio. AUSENTE: tabela, entidade JPA, dominio, use cases e endpoints.

Atualização após main: ✔ RESOLVIDO. `V7__create_prestacao_servico.sql` agora cria `prestacao_servicos` com `id`, `status`, `preco_mdo`, `subtotal`, `data_inicio`, `data_fim`, `ordem_servico_id`, `servico_id`, FKs para `ordem_servicos(id)` e `servicos(id)`, UNIQUE `(ordem_servico_id, servico_id)` e índices por OS/serviço. Existem entidade, domínio, repository/use cases e `PrestacaoServicoController`.

### `alocacao_pecas`

`V8__create_alocacao_pecas.sql` contem DDL comentado. A tabela nao sera criada pelo Flyway. O DDL comentado depende de `prestacao_servicos(id)` e `pecas(id)`.

Atualização após main: ✔ RESOLVIDO. `V8__create_alocacao_pecas.sql` agora cria `alocacao_pecas` com `id`, `status`, `quantidade_necessaria`, `prestacao_servico_id`, `peca_id`, FKs para `prestacao_servicos(id)` e `pecas(id)`, UNIQUE `(prestacao_servico_id, peca_id)` e índices por prestação/peça. Existem entidade, domínio, repository/use case e `AlocacaoPecaController`.

## 4. Entidades JPA e divergências com banco

Base comum:

- `PrincipalEntity`: `status` `@Enumerated(EnumType.STRING)`, `nullable=false`, default Java `StatusRecursoEnum.ATIVO`.
- `Principal` no dominio tambem tem `status=ATIVO`, com `ativar`, `inativar`, `isAtivo`, `isInativo`.

Entidades:

- `UsuarioEntity`: mapeia `usuarios`; `id`, `nome`, `email`, `senha`, `cargoEnum`.
- `ClienteEntity`: mapeia `clientes`; unique constraints para `cpf`, `cnpj`, `email`; `usuarioId` simples, sem relacionamento JPA.
- `VeiculoEntity`: mapeia `veiculos`; `clienteId` simples, sem `@ManyToOne`.
- `PecaEntity`: mapeia `pecas`; `valorUnitario`, `quantidadeEstoque`.
- `OrdemServicoEntity`: mapeia `ordem_servicos`; `veiculoId`, `usuarioId` simples, sem `@ManyToOne`.

Divergencias e riscos:

- `VeiculoMapper.toEntity` nao seta `id`, `status` nem `clienteId`. Criacao de veiculo tende a falhar por `cliente_id NOT NULL`. Atualizacao, caso usada, criaria novo registro sem preservar ID.
- `UsuarioMapper.toEntity` nao seta `id` nem `status`. Criacao funciona com default `ATIVO`, mas alteracoes futuras perderiam identidade.
- `ClienteMapper.toEntity` nao seta `id` nem `status`. `AlterarClienteUseCase` pode criar novo cliente em vez de atualizar o existente e ainda pode violar UNIQUE de email/cpf/cnpj.
- `OrdemServicoMapper.toEntity` nao seta `id` nem `status`. Criacao funciona com default `ATIVO`, mas alteracoes futuras perderiam identidade.
- `PecaMapper.toEntity` seta `id` e `status`; e a entidade mais consistente para update.
- `ClienteEntity.usuarioId` nao tem FK no banco nem relacionamento JPA; o use case de cadastro de cliente nao valida se o usuario existe.
- `OrdemServicoEntity.situacao` usa enum, mas o banco nao possui CHECK para os valores de `SituacaoOrdemServico`.
- `CargoEnum` possui `ALMOXARIFE`; `PecaController` exige role `ALMOXARIFADO`, que nao existe no enum nem no CHECK do banco. Endpoints de pecas ficam inacessiveis para usuarios criados pela propria aplicacao.
- `VeiculoIncluirRequest.ano` e `String`, mas usa `@Min` e `@Max`, que sao constraints numericas. Isso pode causar `UnexpectedTypeException` em runtime em vez de validacao 400 estruturada.

Atualização após main:

- `VeiculoMapper.toEntity` agora seta `clienteId`. ✔ RESOLVIDO para cadastro de veículo com FK obrigatória.
- `PecaController` agora usa `hasRole('ALMOXARIFE')`. ✔ RESOLVIDO.
- `ClienteMapper.toEntity` continua sem setar `id` e `status`. ❌ AINDA PROBLEMA para `PATCH /clientes/{id}`.
- `ServicoMapper.toEntity`, `PrestacaoServicoMapper.toEntity`, `AlocacaoPecaMapper.toEntity` e `OrdemServicoMapper.toEntity` também não preservam `id`/`status`. ⚠️ PARCIALMENTE RESOLVIDO: criação funciona com defaults, mas operações de ativar/inativar/finalizar/alterar podem inserir novo registro ou perder estado.
- `PrestacaoServicoEntity.dataInicio` é `nullable=false` e a migration exige `data_inicio NOT NULL`, mas o construtor `PrestacaoServico` não chama `iniciarServico()` nem seta `dataInicio`. ❌ AINDA PROBLEMA: `POST /prestacao-servico` tende a falhar por `data_inicio` nulo.
- `ServicoJpaRepository.findByNome` está declarado como `Optional<Servico>` em um `JpaRepository<ServicoEntity, UUID>`. ❌ AINDA PROBLEMA: tipo de retorno incompatível com a entidade gerenciada; pode falhar na criação do proxy/repository ou no uso.
- `PrestacaoServicoJpaRepository.findAllByOrdemServicoId(UUID ordemSevicoId)` possui apenas typo no nome do parâmetro, não no método derivado. Baixo risco.

## 5. Domínio e regras de negócio

Enums:

- `StatusRecursoEnum`: `ATIVO`, `INATIVO`.
- `CargoEnum`: `ATENDENTE`, `MECANICO`, `ALMOXARIFE`.
- `SituacaoOrdemServico`: `RECEBIDA`, `EM_DIAGNOSTICO`, `AGUARDANDO_APROVACAO`, `EM_EXECUCAO`, `FINALIZADA`, `ENTREGUE`.

Value objects:

- `Email`: obrigatorio, trim/lowercase, regex simples.
- `CPF`: obrigatorio, remove nao digitos, valida tamanho e digitos.
- `CNPJ`: nao aceita blank, remove nao alfanumericos, uppercase, valida formato alfanumerico e digitos. Risco: construtor chama `value.isBlank()` antes de verificar null.
- `Telefone`: obrigatorio quando instanciado, remove nao digitos, exige 10 ou 11 digitos.
- `Placa`: obrigatoria, normaliza para uppercase, aceita formato antigo e Mercosul. Lanca `IllegalArgumentException`, nao `RegraNegocioException`.

Entidades de dominio:

- `Usuario`: exige nome, email, senha hash e cargo.
- `Cliente`: exige nome, usuarioId e exatamente um documento entre CPF/CNPJ.
- `Veiculo`: exige placa, marca, modelo, ano, quantidadeEixos e clienteId. Nao valida quantidade negativa.
- `Peca`: exige nome/marca nao vazios, valor >= 0, estoque >= 0.
- `OrdemServico`: exige veiculoId e usuarioId; cria com `situacao=RECEBIDA` e `dataRecebida=now()`.

Transicoes de `OrdemServico` no dominio:

- `iniciarDiagnostico`: `RECEBIDA -> EM_DIAGNOSTICO`.
- `iniciarExecucao(valorTotal)`: `EM_DIAGNOSTICO -> EM_EXECUCAO`, valor > 0, `pago=false`.
- `finalizar`: `EM_EXECUCAO -> FINALIZADA`.
- `entregar(pago)`: `FINALIZADA -> ENTREGUE`, exige `pago=true`.

AUSENTE: endpoints/use cases para executar essas transicoes.

Atualização após main: ainda não há endpoints diretos para `OrdemServico.iniciarDiagnostico`, `iniciarExecucao`, `finalizar` ou `entregar`. Foram adicionados endpoints de `PrestacaoServico` para `ativar`, `inativar` e `finalizar`, mas eles alteram `PrestacaoServico`, não a situação da OS.

Excecoes de negocio:

- `RegraNegocioException`: regras de dominio e duplicidade controlada.
- `RecursoNaoEncontradoException`: consultas sem resultado.
- `IllegalArgumentException`: pode sair de `Placa`, mas nao ha handler especifico.

Atualização após main: foi adicionado `Senha` como value object com regras mínimas para cadastro de usuário: senha obrigatória, mínimo 8 caracteres, máximo 100, ao menos um número e ao menos um caractere especial. Isso afeta os mocks: a senha antiga `123456` não passa mais pelo cadastro via API, embora ainda possa ser usada em inserts SQL se o hash for válido.

## 6. Use cases existentes

Usuarios:

- `CadastrarUsuarioUseCase.executar(senha, nome, email, cargoEnum)`: verifica duplicidade por email, gera BCrypt, cria `Usuario`. Mensagem de duplicidade incorreta: `"Placa ja cadastrada"`.
- `BuscarUsuarioPorEmailUseCase.executar(email)`: 404 quando ausente.
- `AutenticarUsuarioUseCase.login(email, senha)`: busca usuario, valida BCrypt, gera JWT.

Clientes:

- `CadastrarClienteUseCase.executar(nome, cpf, cnpj, email, telefone, usuarioId)`: cria VOs e salva. Nao valida existencia do usuario.
- `AlterarClienteUseCase.executar(id, nome, telefone)`: busca cliente, altera nome/telefone e salva.
- `BuscarClientePorCpfUseCase.executar(cpf)`: cria `CPF`, busca por CPF.
- `BuscarClientePorCnpjUseCase.executar(cnpj)`: cria `CNPJ`, busca por CNPJ.
- `BuscarClientePorUsuarioIdUseCase.executar(usuarioId)`: retorna um unico cliente por usuarioId.

Veiculos:

- `CadastrarVeiculoUseCase.executar(placa, marca, modelo, ano, quantidadeEixos, clienteId)`: verifica placa duplicada, cria `Veiculo`, salva. Nao valida existencia do cliente antes do insert; depende da FK.
- `BuscarVeiculoPorIdUseCase.executar(id)`: 404 quando ausente.
- `BuscarVeiculoPorPlacaUseCase.executar(placa)`: busca literal por placa; nao normaliza placa antes da consulta.
- `BuscarTodosVeiculosPorClienteIdUseCase.executar(clienteId)`: retorna lista, vazia se nao houver.
- `AlterarVeiculoUseCase`: existe, mas AUSENTE endpoint que o chame.

Pecas:

- `CadastrarPecaUseCase.executar(nome, marca, valorUnitario, quantidadeEstoque)`: verifica nome duplicado, valida dominio, salva.
- `AlterarPecaUseCase.executar(id, nome, marca, valorUnitario, quantidadeEstoque)`: busca por id, verifica duplicidade por nome, altera e salva.
- `BuscarPecaPorIdUseCase.executar(id)`: 404 quando ausente.

Ordens de servico:

- `CadastrarOrdemServicoUseCase.executar(observacao, veiculoId, usuarioId)`: busca usuario, exige `CargoEnum.MECANICO`, cria OS e salva. Nao busca veiculo antes; depende da FK.
- `BuscarOrdemServicoPorIdUseCase.executar(id)`: 404 quando ausente.

Transacoes:

- AUSENTE: `@Transactional` nos use cases. Risco moderado em fluxos de update e em futuras operacoes multi-repository.

Atualização após main:

- `CadastrarClienteUseCase` agora usa `CurrentUserProvider` e remove `usuarioId` do request. ✔ RESOLVIDO para o risco de cliente ser associado a usuário arbitrário pelo payload.
- `CadastrarOrdemServicoUseCase` agora usa `CurrentUserProvider` e remove `usuarioId` do request. ✔ RESOLVIDO para o risco de OS ser criada em nome de outro mecânico informado no payload.
- `CadastrarUsuarioUseCase` valida senha com `Senha` e normaliza email antes da busca. ✔ RESOLVIDO parcialmente para força mínima de senha e duplicidade por variação de caixa.
- `CadastrarClienteUseCase` chama `new CPF(cpf)` e `new CNPJ(cnpj)` antes de verificar null; como o fluxo exige exatamente um documento, chamadas com um dos campos nulo podem gerar erro antes da regra esperada. ❌ AINDA PROBLEMA.
- Foram adicionados `CadastrarServicoUseCase`, `AlterarServicoUseCase`, `AtivarServicoUseCase`, `InativarServicoUseCase`, `CadastrarPrestacaoServicoUseCase`, `AtivarPrestacaoServicoUseCase`, `InativarPrestacaoServicoUseCase`, `FinalizarPrestacaoServicoUseCase`, `CadastrarAlocacaoPecaUseCase`, `BuscarTodosOrdemServicoPorVeiculoIdUseCase` e `ConsultarTempoMedioOSUseCase`.
- AUSENTE: `@Transactional` continua ausente. ❌ AINDA PROBLEMA, especialmente em alocação de peças/estoque e ativações/finalizações.

## 7. Segurança e autenticação

`SecurityConfig`:

- CSRF desabilitado.
- Sessao stateless.
- Publicos por path: `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`.
- Publico adicional: `POST /usuario`.
- Demais rotas exigem autenticacao.
- Method security habilitado por `@EnableMethodSecurity`.

Risco critico: o controller de cadastro de usuario esta em `@RequestMapping("/usuarios")`, mas o matcher publico libera `POST /usuario` singular. Portanto `POST /usuarios` exige JWT, apesar de nao ter `@PreAuthorize`. Com banco vazio, o fluxo documentado no README ("criar usuario, fazer login") fica bloqueado.

`JwtAuthenticationFilter`:

- Espera header `Authorization: Bearer <token>`.
- Remove prefixo `Bearer ` e chama `jwtService.extractUsername(token)`.
- Carrega usuario por email e seta `SecurityContext` se token valido.
- Imprime username e authorities com `System.out.println`.

`JwtService`:

- JWT subject = email.
- Expiracao = 1 hora.
- Assinatura HMAC com segredo Base64 de `jwt.secret`.
- Nao inclui roles como claims; role vem do banco a cada request.

`CustomUserDetailsService`:

- Authority gerada como `"ROLE_" + usuario.getCargoEnum().name()`.
- Roles possiveis reais: `ROLE_ATENDENTE`, `ROLE_MECANICO`, `ROLE_ALMOXARIFE`.

Roles exigidas:

- Clientes e veiculos: `hasRole('ATENDENTE')`.
- Ordens de servico: `hasRole('MECANICO')`.
- Pecas: `hasRole('ALMOXARIFADO')`.

INCERTEZA TECNICA: `ALMOXARIFADO` parece ser erro de nomenclatura, pois o enum, o banco e o Swagger de usuario indicam `ALMOXARIFE`.

Atualização após main:

- `POST /usuarios` agora está público corretamente. ✔ RESOLVIDO.
- Endpoints de peças e alocação de peças usam `hasRole('ALMOXARIFE')`. ✔ RESOLVIDO.
- `JwtAuthenticationFilter` não contém mais `System.out.println`. ✔ RESOLVIDO.
- `jwt.secret` continua hardcoded no `application.properties`; o compose também contém `JWT_SECRET` literal. ❌ AINDA PROBLEMA.
- `GlobalExceptionHandler` agora retorna `ErrorResponse` para validação, JSON inválido, recurso não encontrado, senha inválida, regra de negócio, integridade, genérico e autorização. ⚠️ PARCIALMENTE RESOLVIDO: melhora contrato de erro, mas ainda vaza causa de banco no handler de `DataIntegrityViolationException` e loga stacktrace completo com `log.error("Erro: ", ex)`.
- `CurrentUserProvider` foi adicionado para recuperar o usuário autenticado pelo `SecurityContext`.

## 8. Catálogo de endpoints

| Metodo | Path | Controller.metodo | Auth | Role | Request | Response | Status atual esperado |
|---|---|---|---|---|---|---|---:|
| POST | `/auth/login` | `AuthController.login` | publico | - | `LoginRequest` | `{ "token": "..." }` | 200 |
| POST | `/usuarios` | `UsuarioController.cadastrar` | protegido na pratica | autenticado sem role especifica | `UsuarioIncluirRequest` | `UsuarioResponse` | 200 |
| POST | `/clientes` | `ClienteController.cadastrar` | JWT | `ATENDENTE` | `ClienteIncluirRequest` | `ClienteResponse` | 200 |
| PATCH | `/clientes/{id}` | `ClienteController.alterar` | JWT | `ATENDENTE` | `ClienteAlterarRequest` | `ClienteResponse` | 200 |
| GET | `/clientes/por-cpf/{cpf}` | `ClienteController.buscarPorCpf` | JWT | `ATENDENTE` | - | `ClienteResponse` | 200 |
| GET | `/clientes/por-cnpj/{cnpj}` | `ClienteController.buscarPorCnpj` | JWT | `ATENDENTE` | - | `ClienteResponse` | 200 |
| GET | `/clientes/por-usuario/{usuarioId}` | `ClienteController.buscarTodosPorUsuarioId` | JWT | `ATENDENTE` | - | `ClienteResponse` | INCERTEZA |
| POST | `/veiculos` | `VeiculoController.cadastrar` | JWT | `ATENDENTE` | `VeiculoIncluirRequest` | `VeiculoResponse` | 200, mas risco de 400 por FK/not null |
| GET | `/veiculos/{id}` | `VeiculoController.buscarPorId` | JWT | `ATENDENTE` | - | `VeiculoResponse` | 200 |
| GET | `/veiculos/por-placa/{placa}` | `VeiculoController.buscarPorPlaca` | JWT | `ATENDENTE` | - | `VeiculoResponse` | 200 |
| GET | `/veiculos/cliente/{clienteId}` | `VeiculoController.buscarPorClienteId` | JWT | `ATENDENTE` | - | `List<VeiculoResponse>` | 200 |
| POST | `/pecas` | `PecaController.cadastrar` | JWT | `ALMOXARIFADO` | `PecaIncluirRequest` | `PecaResponse` | 403 para usuarios reais |
| PUT | `/pecas/{id}` | `PecaController.alterar` | JWT | `ALMOXARIFADO` | `PecaAtualizarRequest` | `PecaResponse` | 403 para usuarios reais |
| GET | `/pecas/{id}` | `PecaController.buscarPorId` | JWT | `ALMOXARIFADO` | - | `PecaResponse` | 403 para usuarios reais |
| POST | `/ordem-servicos` | `OrdemServicoController.cadastrar` | JWT | `MECANICO` | `OrdemServicoIncluirRequest` | `OrdemServicoResponse` | 200 |
| GET | `/ordem-servicos/{id}` | `OrdemServicoController.buscarPorId` | JWT | `MECANICO` | - | `OrdemServicoResponse` | 200 |

Observacoes:

- Todos os endpoints de criacao retornam `200 OK`, nao `201 Created`.
- `LoginResponse` existe com campo `jwt`, mas `AuthController` retorna `Map.of("token", token)`.
- `ClienteController.buscarTodosPorUsuarioId` nao possui `@PathVariable` no parametro `UUID usuarioId`; pode falhar no binding em runtime.
- `@RequestMapping("ordem-servicos")` nao inicia com `/`, mas Spring normalmente expoe `/ordem-servicos`.

Atualização após main - mudanças no catálogo:

- `POST /usuarios`: Auth corrigido para público. Status documental anterior "protegido na pratica" está obsoleto.
- `/pecas`: role corrigida de `ALMOXARIFADO` para `ALMOXARIFE`; status esperado deixa de ser 403 para usuários reais com cargo `ALMOXARIFE`.
- `/veiculos/cliente/{clienteId}` mudou para `/veiculos/por-cliente/{clienteId}`.
- `POST /clientes`: request não tem mais `usuarioId`; usa usuário autenticado via `CurrentUserProvider`.
- `POST /ordem-servicos`: request não tem mais `usuarioId`; usa usuário autenticado via `CurrentUserProvider`.
- `GET /clientes/por-usuario/{usuarioId}` continua sem `@PathVariable` no parâmetro e permanece como problema.

Endpoints adicionados após main:

| Metodo | Path | Controller.metodo | Auth | Role | Request | Response | Status atual esperado |
|---|---|---|---|---|---|---|---:|
| POST | `/servicos` | `ServicoController.cadastrar` | JWT | `MECANICO` | `ServicoIncluirRequest` | `ServicoResponse` | 200 |
| PUT | `/servicos/{id}` | `ServicoController.alterar` | JWT | `MECANICO` | `ServicoIncluirRequest` | `ServicoResponse` | 200, com risco por mapper não preservar id |
| PATCH | `/servicos/{id}/ativar` | `ServicoController.ativar` | JWT | `MECANICO` | - | `ServicoResponse` | 200, com risco por mapper não preservar id/status |
| PATCH | `/servicos/{id}/inativar` | `ServicoController.inativar` | JWT | `MECANICO` | - | `ServicoResponse` | 200, com risco por mapper não preservar id/status |
| POST | `/prestacao-servico` | `PrestacaoServicoController.cadastrar` | JWT | `MECANICO` | `PrestacaoServicoIncluirRequest` | `PrestacaoServicoResponse` | risco de 400/500 por `data_inicio` nulo |
| GET | `/prestacao-servico/por-ordem-servico/{ordemServicoId}` | `PrestacaoServicoController.buscarTodosPorOrdemServicoId` | JWT | `MECANICO` | - | `List<PrestacaoServicoResponse>` | 200 |
| PATCH | `/prestacao-servico/{id}/ativar` | `PrestacaoServicoController.ativar` | JWT | `MECANICO` | - | `PrestacaoServicoResponse` | 200, com risco por mapper não preservar id/status |
| PATCH | `/prestacao-servico/{id}/inativar` | `PrestacaoServicoController.inativar` | JWT | `MECANICO` | - | `PrestacaoServicoResponse` | 200, com risco por mapper não preservar id/status |
| PATCH | `/prestacao-servico/{id}/finalizar` | `PrestacaoServicoController.finalizar` | JWT | `MECANICO` | - | `PrestacaoServicoResponse` | 200, com risco por mapper não preservar id/status |
| POST | `/alocacao-pecas` | `AlocacaoPecaController.incluir` | JWT | `ALMOXARIFE` | `AlocacaoPecaIncluirRequest` | `AlocacaoPecaResponse` | 200 |
| GET | `/ordem-servicos/veiculo/{veiculoId}` | `OrdemServicoController.buscarTodosPorVeiculoId` | JWT | `MECANICO` | - | `List<OrdemServicoResponse>` | 200 |
| GET | `/ordem-servicos/{id}/tempo-medio` | `OrdemServicoController.buscarTempoMedioDosServicos` | JWT | `MECANICO` | - | `TempoMedioOSResponse` | 200 |

## 9. Plano de testes manuais por endpoint

### POST `/auth/login`

- Objetivo: autenticar usuario e obter JWT.
- Pre-condicao: usuario existente com senha BCrypt.
- Role: publico.
- Payload valido: `{"email":"atendente@mecanica.test","senha":"123456"}`.
- Invalidos: email inexistente; senha incorreta; email null; senha null; JSON vazio; token nao se aplica.
- Esperado: 200 com `token`; 404 para usuario inexistente; 404 para senha invalida por handler atual.
- Riscos: credencial invalida deveria retornar 401, nao 404; request nao usa `@Valid`; null pode gerar excecao nao tratada dependendo do fluxo.

### POST `/usuarios`

- Objetivo: cadastrar usuario.
- Pre-condicao: na configuracao atual, exige JWT por falha no matcher publico.
- Role: autenticado, sem `@PreAuthorize`.
- Payload valido: `{"nome":"Novo Atendente","email":"novo.atendente@mecanica.test","senha":"123456","cargoEnum":"ATENDENTE"}`.
- Invalidos: nome vazio; email invalido; senha vazia; cargo null; cargo inexistente; email duplicado.
- Esperado: 200 com `id`, `statusRecursoEnum`, `nome`, `email`, `cargoEnum`.
- Riscos: criacao publica pretendida esta bloqueada; se corrigida, permite criar qualquer cargo, inclusive mecanico/almoxarife, sem administracao; duplicidade retorna mensagem incorreta `"Placa ja cadastrada"`; endpoint retorna 200 em vez de 201.

### POST `/clientes`

- Objetivo: cadastrar cliente PF ou PJ.
- Pre-condicao: JWT de `ATENDENTE`; para integridade logica, `usuarioId` deve ser de usuario existente.
- Payload valido PF: `{"nome":"Cliente CPF","cpf":"52998224725","cnpj":null,"email":"cliente.cpf@mecanica.test","telefone":"11987654321","usuarioId":"11111111-1111-1111-1111-111111111111"}`.
- Payload valido PJ: `{"nome":"Cliente PJ","cpf":null,"cnpj":"11222333000181","email":"cliente.pj@mecanica.test","telefone":"1133334444","usuarioId":"11111111-1111-1111-1111-111111111111"}`.
- Invalidos: cpf e cnpj ambos null; cpf e cnpj ambos preenchidos; cpf invalido; cnpj invalido; email invalido; nome vazio; usuarioId null; email/cpf/cnpj duplicado; telefone com menos de 10 digitos.
- Esperado: 200 com `ClienteResponse`; 400 para validacao/regra/unique; 403 com role errada.
- Riscos: banco nao valida FK de `usuario_id`; telefone null e aceito; endpoint permite associar cliente a usuario inexistente via API.

### PATCH `/clientes/{id}`

- Objetivo: alterar nome e telefone.
- Pre-condicao: cliente existente; JWT de `ATENDENTE`.
- Payload valido: `{"nome":"Cliente CPF Alterado","telefone":"11999998888"}`.
- Invalidos: UUID inexistente; nome null; telefone invalido; body vazio.
- Esperado: 200 com `ClienteResponse`; 404 para id inexistente; 400 para telefone invalido.
- Riscos: `ClienteAlterarRequest` nao tem Bean Validation; dominio valida `nome == null`, mas aceita blank; `ClienteMapper.toEntity` nao preserva `id`, podendo tentar inserir novo cliente e causar violacao de unique em vez de atualizar.

### GET `/clientes/por-cpf/{cpf}`

- Objetivo: buscar cliente por CPF.
- Pre-condicao: cliente PF existente; JWT de `ATENDENTE`.
- Valido: `/clientes/por-cpf/52998224725`.
- Invalidos: CPF inexistente valido; CPF invalido; CPF vazio nao aplicavel em path.
- Esperado: 200 com cliente; 404 se nao encontrado; 400 se CPF invalido.
- Riscos: `CPF` invalido gera `RegraNegocioException` tratada como 400.

### GET `/clientes/por-cnpj/{cnpj}`

- Objetivo: buscar cliente por CNPJ.
- Pre-condicao: cliente PJ existente; JWT de `ATENDENTE`.
- Valido: `/clientes/por-cnpj/11222333000181`.
- Invalidos: CNPJ inexistente valido; CNPJ invalido.
- Esperado: 200 com cliente; 404 se nao encontrado; 400 se invalido.
- Riscos: `CNPJ` null nao se aplica por path; se chamado internamente com null causaria NPE nao tratada.

### GET `/clientes/por-usuario/{usuarioId}`

- Objetivo: buscar cliente por usuarioId.
- Pre-condicao: cliente associado a `usuarioId`; JWT de `ATENDENTE`.
- Valido: `/clientes/por-usuario/11111111-1111-1111-1111-111111111111`.
- Invalidos: UUID inexistente; UUID malformado.
- Esperado: INCERTEZA TECNICA. Pela intencao, 200 com `ClienteResponse`; pelo codigo, falta `@PathVariable UUID usuarioId`, podendo falhar binding.
- Riscos: metodo e operation dizem "Cadastrar um cliente"; repository retorna apenas um cliente, apesar do nome `buscarTodos`; sem validacao de ownership/autorizacao por usuario.

### POST `/veiculos`

- Objetivo: cadastrar veiculo.
- Pre-condicao: cliente existente; JWT de `ATENDENTE`.
- Payload valido: `{"placa":"ABC1D23","marca":"Toyota","modelo":"Corolla","ano":"2020","quantidadeEixos":2,"clienteId":"33333333-3333-3333-3333-333333333333"}`.
- Invalidos: placa invalida; placa duplicada; marca/modelo/ano vazios; ano fora de 1000-9999; quantidadeEixos null; quantidadeEixos negativa; clienteId null; clienteId inexistente.
- Esperado: 200 se mapper persistir corretamente; 400 para duplicidade/validacao/FK.
- Riscos: `VeiculoMapper.toEntity` nao seta `clienteId`, entao mesmo payload valido tende a violar `cliente_id NOT NULL`; `@Min/@Max` em `String ano` pode causar erro 500; quantidade negativa nao e validada.

### GET `/veiculos/{id}`

- Objetivo: buscar veiculo por ID.
- Pre-condicao: veiculo existente; JWT de `ATENDENTE`.
- Invalidos: UUID inexistente; UUID malformado.
- Esperado: 200 com `VeiculoResponse`; 404 se inexistente.
- Riscos: se dados forem inseridos direto com placa invalida, mapper para dominio pode lancar `IllegalArgumentException` sem handler.

### GET `/veiculos/por-placa/{placa}`

- Objetivo: buscar veiculo por placa.
- Pre-condicao: veiculo existente; JWT de `ATENDENTE`.
- Valido: `/veiculos/por-placa/ABC1D23`.
- Invalidos: placa inexistente; placa com separador/lowercase.
- Esperado: 200 se string bater exatamente com banco; 404 se nao encontrado.
- Riscos: busca nao normaliza antes do repository; `abc-1d23` pode nao encontrar mesmo sendo placa equivalente normalizada.

### GET `/veiculos/cliente/{clienteId}`

- Objetivo: listar veiculos de um cliente.
- Pre-condicao: cliente com zero ou mais veiculos; JWT de `ATENDENTE`.
- Invalidos: UUID malformado.
- Esperado: 200 com lista; lista vazia para cliente inexistente ou sem veiculos.
- Riscos: nao diferencia cliente inexistente de cliente sem veiculos.

### POST `/pecas`

- Objetivo: cadastrar peca.
- Pre-condicao: role esperada pelo codigo `ALMOXARIFADO`; na base atual nao ha como criar essa role via enum.
- Payload valido: `{"nome":"Filtro de Oleo","marca":"Bosch","valorUnitario":39.90,"quantidadeEstoque":10}`.
- Invalidos: nome vazio; marca vazia; valor null; valor negativo; estoque null; estoque negativo; nome duplicado.
- Esperado: 403 para usuarios `ALMOXARIFE` por divergencia de role; se role fosse corrigida, 200 com `PecaResponse`.
- Riscos: endpoints de pecas inacessiveis; banco permite negativos se inseridos por SQL; retorna 200 em criacao.

### PUT `/pecas/{id}`

- Objetivo: atualizar peca.
- Pre-condicao: peca existente; role `ALMOXARIFADO` pelo codigo.
- Payload valido: `{"nome":"Filtro de Oleo Premium","marca":"Bosch","valorUnitario":49.90,"quantidadeEstoque":15}`.
- Invalidos: UUID inexistente; nome duplicado de outra peca; campos null/vazios; negativos.
- Esperado: 403 para usuarios reais; se role corrigida, 200 ou 404/400 conforme caso.
- Riscos: bloqueado por role inexistente; update preserva ID no mapper de peca, diferentemente de outros agregados.

### GET `/pecas/{id}`

- Objetivo: buscar peca por ID.
- Pre-condicao: peca existente; role `ALMOXARIFADO` pelo codigo.
- Invalidos: UUID inexistente; UUID malformado.
- Esperado: 403 para usuarios reais; se role corrigida, 200 ou 404.
- Riscos: bloqueado por role inexistente.

### POST `/ordem-servicos`

- Objetivo: criar ordem de servico recebida.
- Pre-condicao: JWT de `MECANICO`; `usuarioId` deve existir e ser `MECANICO`; `veiculoId` deve existir.
- Payload valido: `{"observacao":"Barulho ao frear","veiculoId":"44444444-4444-4444-4444-444444444444","usuarioId":"22222222-2222-2222-2222-222222222222"}`.
- Invalidos: veiculoId null; usuarioId null; usuarioId inexistente; usuarioId de atendente/almoxarife; veiculoId inexistente.
- Esperado: 200 com `situacao=RECEBIDA`; 404 usuario inexistente; 400 usuario nao mecanico; 400 por FK se veiculo inexistente.
- Riscos: o token pode ser de um mecanico A e o payload informar mecanico B; nao ha comparacao com usuario autenticado. Veiculo nao e validado antes do insert.

### GET `/ordem-servicos/{id}`

- Objetivo: buscar OS por ID.
- Pre-condicao: OS existente; JWT de `MECANICO`.
- Invalidos: UUID inexistente; UUID malformado.
- Esperado: 200 com `OrdemServicoResponse`; 404 se inexistente.
- Riscos: somente mecanico acessa; cliente nao tem endpoint para consultar situacao, apesar do TODO no controller.

Testes transversais obrigatorios:

- Sem token em endpoint protegido: esperado 403/401 conforme Spring Security; validar comportamento real.
- Token malformado/expirado: risco de excecao no filtro sem resposta padronizada.
- Token de role errada: esperado 403.
- `Content-Type` ausente/incorreto em POST/PUT/PATCH: validar 415/400.
- UUID malformado em path: sem handler especifico; pode retornar 400 default.
- Duplicidades por constraints: usuario email, cliente cpf/cnpj/email, veiculo placa, peca nome.

Atualização após main - ajustes no plano de testes:

- `POST /usuarios`: agora deve ser testado sem token. Payload precisa usar senha forte, por exemplo `Senha@123`, pois o novo value object `Senha` exige mínimo 8 caracteres, número e caractere especial.
- `POST /pecas`, `PUT /pecas/{id}` e `GET /pecas/{id}`: testar com token de `ALMOXARIFE`; o bloqueio anterior por role inexistente foi corrigido.
- `GET /veiculos/cliente/{clienteId}`: endpoint antigo deve retornar 404; usar o novo path `GET /veiculos/por-cliente/{clienteId}`.
- `POST /clientes`: remover `usuarioId` do payload; testar se o cliente fica associado ao usuário autenticado. Risco atual: `CadastrarClienteUseCase` instancia `CPF` e `CNPJ` antes da checagem de null, então testar PF-only e PJ-only explicitamente.
- `POST /ordem-servicos`: remover `usuarioId` do payload; testar com token `MECANICO` e com token `ATENDENTE` para validar 403/422. O `usuarioId` da resposta deve ser o usuário autenticado.

Novos testes manuais por endpoint:

### POST `/servicos`

- Objetivo: cadastrar serviço base.
- Pre-condição: JWT de `MECANICO`.
- Payload válido: `{"nome":"Troca de óleo","descricao":"Substituição de óleo e filtro"}`.
- Inválidos: nome vazio/null; nome duplicado.
- Esperado: 200 com `ServicoResponse`; 422 para duplicidade/regra; 403 com role errada.
- Riscos: retorna 200 em vez de 201.

### PUT `/servicos/{id}`

- Objetivo: alterar nome/descrição de serviço.
- Pre-condição: serviço existente; JWT de `MECANICO`.
- Payload válido: `{"nome":"Troca de óleo premium","descricao":"Óleo sintético"}`.
- Inválidos: UUID inexistente; nome duplicado; nome vazio.
- Esperado: 200, 404 ou 422 conforme caso.
- Riscos: `ServicoMapper.toEntity` não preserva `id`; update pode inserir novo registro ou violar unique.

### PATCH `/servicos/{id}/ativar` e `/servicos/{id}/inativar`

- Objetivo: alternar status do serviço.
- Pre-condição: serviço existente; JWT de `MECANICO`.
- Inválidos: UUID inexistente; role errada.
- Esperado: 200 com status alterado.
- Riscos: mapper não preserva `id`/`status`; validar se realmente atualiza a linha esperada.

### POST `/prestacao-servico`

- Objetivo: adicionar serviço executado em uma OS.
- Pre-condição: OS ativa existente; serviço ativo existente; JWT de `MECANICO`.
- Payload válido: `{"precoMaoDeObra":150.00,"ordemServicoId":"66666666-6666-6666-6666-666666666666","servicoId":"77777777-7777-7777-7777-777777777777"}`.
- Inválidos: preço null/zero/negativo; OS inexistente/inativa; serviço inexistente/inativo; duplicidade `(ordemServicoId, servicoId)`.
- Esperado: intenção é 200, mas o código atual tende a falhar porque `dataInicio` é obrigatório no banco/entity e não é setado no construtor.
- Riscos: `@NotNull` não impede zero/negativo no DTO; domínio barra `<= 0`.

### GET `/prestacao-servico/por-ordem-servico/{ordemServicoId}`

- Objetivo: listar prestações por OS.
- Pre-condição: JWT de `MECANICO`.
- Inválidos: UUID malformado.
- Esperado: 200 com lista, vazia se não houver.
- Riscos: não diferencia OS inexistente de OS sem prestações.

### PATCH `/prestacao-servico/{id}/ativar`, `/inativar`, `/finalizar`

- Objetivo: alterar status ou finalizar prestação.
- Pre-condição: prestação existente; JWT de `MECANICO`.
- Inválidos: UUID inexistente; prestação inativa ao finalizar; prestação já finalizada.
- Esperado: 200 ou 404/422.
- Riscos: mapper não preserva `id`/`status`; `finalizar` exige `dataInicio` não nulo semanticamente, mas domínio só impede inativo e `dataFim` repetido.

### POST `/alocacao-pecas`

- Objetivo: alocar peça em uma prestação de serviço.
- Pre-condição: prestação existente; peça existente com estoque suficiente; JWT de `ALMOXARIFE`.
- Payload válido: `{"quantidadeNecessaria":2,"prestacaoServicoId":"88888888-8888-8888-8888-888888888888","pecaId":"55555555-5555-5555-5555-555555555555"}`.
- Inválidos: quantidade null/zero/negativa; prestação inexistente; peça inexistente; peça duplicada na mesma prestação; quantidade maior que estoque.
- Esperado: 200 ou 404/422.
- Riscos: DTO usa `@PositiveOrZero`, mas domínio exige `> 0`; use case não valida existência da prestação antes do insert, depende de FK.

### GET `/ordem-servicos/veiculo/{veiculoId}`

- Objetivo: listar OS por veículo.
- Pre-condição: veículo com zero ou mais OS; JWT de `MECANICO`.
- Esperado: 200 com lista.
- Riscos: não diferencia veículo inexistente de veículo sem OS.

### GET `/ordem-servicos/{id}/tempo-medio`

- Objetivo: consultar tempo médio dos serviços relacionados à OS.
- Pre-condição: OS com prestações finalizadas historicamente para os mesmos serviços; JWT de `MECANICO`.
- Esperado: 200 com `servicos` e `tempoTotal`.
- Riscos: se a OS não tiver prestações, retorna lista vazia/tempo calculado sobre conjunto vazio; validar comportamento real do repository com `IN (:servicoIds)` vazio.

## 10. Dados necessários para V9__mock_dados.sql

Senha padrao sugerida para todos os usuarios: `123456`.

Atualização após main: para criação via API, a senha `123456` não é mais válida porque `Senha` exige mínimo 8 caracteres, número e caractere especial. Para a migration V9, pode-se inserir qualquer hash BCrypt diretamente; para alinhar testes manuais com API, recomenda-se usar senha padrão `Senha@123` e gerar hash BCrypt correspondente antes de criar a migration.

Hash BCrypt gerado com `BCryptPasswordEncoder` para `123456`:

```text
$2a$10$zYmvy4evnMoziyiM8ToaVeb8YG7SPh2mDb9OMbN9Po0h/0TCgNIDW
```

UUIDs fixos sugeridos:

| Entidade | UUID | Observacao |
|---|---|---|
| Usuario atendente | `11111111-1111-1111-1111-111111111111` | `ATENDENTE`, login `atendente@mecanica.test` |
| Usuario mecanico | `22222222-2222-2222-2222-222222222222` | `MECANICO`, login `mecanico@mecanica.test` |
| Usuario almoxarife | `aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa` | `ALMOXARIFE`, login `almoxarife@mecanica.test`; nao acessa pecas enquanto controller exigir `ALMOXARIFADO` |
| Cliente PF | `33333333-3333-3333-3333-333333333333` | CPF `52998224725`, usuario atendente |
| Cliente PJ | `33333333-3333-3333-3333-333333333334` | CNPJ `11222333000181`, usuario atendente |
| Veiculo 1 | `44444444-4444-4444-4444-444444444444` | placa `ABC1D23`, cliente PF |
| Veiculo 2 | `44444444-4444-4444-4444-444444444445` | placa `XYZ1234`, cliente PJ |
| Peca 1 | `55555555-5555-5555-5555-555555555555` | `Filtro de Oleo` |
| Peca 2 | `55555555-5555-5555-5555-555555555556` | `Pastilha de Freio` |
| OS recebida | `66666666-6666-6666-6666-666666666666` | veiculo 1, mecanico |
| OS em execucao | `66666666-6666-6666-6666-666666666667` | para testar GET com campos opcionais preenchidos |

Ordem correta de inserts:

1. `usuarios`.
2. `clientes`.
3. `veiculos`.
4. `pecas`.
5. `ordem_servicos`.

Nao inserir:

- `servicos`: tabela ausente.
- `prestacao_servicos`: tabela ausente.
- `alocacao_pecas`: tabela ausente porque migration esta comentada.
- Movimentacoes de estoque: AUSENTE no schema atual.

Atualização após main: a lista acima ficou obsoleta. Agora devem ser inseridos também `servicos`, `prestacao_servicos` e `alocacao_pecas`, pois `V5`, `V7` e `V8` criam essas tabelas. Movimentações de estoque continuam AUSENTES; alocação de peças não baixa estoque automaticamente no código atual.

Dados minimos por tabela:

- `usuarios`: 3 linhas com `status='ATIVO'`, cargos `ATENDENTE`, `MECANICO`, `ALMOXARIFE`.
- `clientes`: 2 linhas, uma PF e uma PJ, emails unicos, exatamente um documento por linha.
- `veiculos`: 2 linhas com placas validas e clientes existentes.
- `pecas`: 2 linhas com valores e estoques nao negativos.
- `ordem_servicos`: pelo menos uma `RECEBIDA` e opcionalmente uma `EM_EXECUCAO`/`FINALIZADA` para testar serializacao de datas e valor.

INCERTEZA TECNICA: nao criar usuario com cargo `ALMOXARIFADO`, pois o CHECK de `usuarios.cargo_enum` rejeita e `CargoEnum` nao possui esse valor.

Atualização após main - dados adicionais para V9:

Ordem correta de inserts atualizada:

1. `usuarios`.
2. `clientes`.
3. `veiculos`.
4. `pecas`.
5. `servicos`.
6. `ordem_servicos`.
7. `prestacao_servicos`.
8. `alocacao_pecas`.

UUIDs fixos adicionais sugeridos:

| Entidade | UUID | Observacao |
|---|---|---|
| Serviço troca de óleo | `77777777-7777-7777-7777-777777777777` | `servicos.nome='Troca de óleo'`, status `ATIVO` |
| Serviço freio | `77777777-7777-7777-7777-777777777778` | `servicos.nome='Revisão de freios'`, status `ATIVO` |
| Prestação serviço 1 | `88888888-8888-8888-8888-888888888888` | OS recebida + serviço troca de óleo |
| Prestação serviço finalizada histórica | `88888888-8888-8888-8888-888888888889` | Usar `data_inicio` e `data_fim` para testar tempo médio |
| Alocação peça 1 | `99999999-9999-9999-9999-999999999999` | Prestação 1 + peça 1 |

Dados obrigatórios novos:

- `servicos`: pelo menos dois serviços ativos e um inativo para testar ativar/inativar e duplicidade.
- `prestacao_servicos`: pelo menos uma prestação ativa com `data_inicio NOT NULL`, uma finalizada com `data_fim`, e uma inativa se desejar testar regras.
- `alocacao_pecas`: pelo menos uma alocação por prestação/peça e uma peça sem alocação para testar criação.

Atenção para V9:

- `prestacao_servicos.data_inicio` é NOT NULL; inserts SQL precisam preencher esse campo.
- `prestacao_servicos` tem UNIQUE `(ordem_servico_id, servico_id)`.
- `alocacao_pecas` tem UNIQUE `(prestacao_servico_id, peca_id)`.
- Como `ClienteMapper`, `ServicoMapper`, `PrestacaoServicoMapper`, `AlocacaoPecaMapper` e `OrdemServicoMapper` não preservam `id`/`status`, dados de mock devem incluir cenários para detectar update que vira insert.

## 11. Vulnerabilidades e riscos encontrados

Criticos:

- Cadastro inicial de usuario bloqueado: `SecurityConfig` libera `POST /usuario`, mas controller e `/usuarios`.
- Endpoints de pecas inacessiveis por divergencia `ALMOXARIFADO` vs `ALMOXARIFE`.
- `VeiculoMapper.toEntity` nao seta `clienteId`, causando falha em cadastro valido de veiculo.
- `ClienteMapper.toEntity` nao preserva `id`, podendo transformar update em insert.

Altos:

- `jwt.secret` hardcoded em arquivo versionado e no compose.
- Possivel criacao de usuarios administrativos/operacionais sem controle de role se `/usuarios` for tornado publico sem regra adicional.
- Logs sensiveis no filtro JWT: imprime usuario e authorities em stdout.
- `DataIntegrityViolationException` retorna detalhe interno do banco: `"Dado enviado incorretamente: " + ex.getMostSpecificCause().getMessage()`.
- `POST /ordem-servicos` aceita `usuarioId` no payload e nao confere se e o mesmo usuario autenticado.
- Ausencia de handler para JWT invalido/expirado e para `IllegalArgumentException`.

Medios:

- Falta rate limiting em `/auth/login`.
- Sem refresh/revogacao de token; JWT dura 1 hora.
- `LoginRequest` nao usa Bean Validation.
- `ClienteAlterarRequest` nao usa Bean Validation.
- `VeiculoIncluirRequest.ano` usa constraints incompativeis com `String`.
- Banco nao possui CHECK para `ordem_servicos.situacao`.
- Banco permite peca com valor/estoque negativos se inserida fora da API.
- `clientes.usuario_id` nao tem FK.
- Sem `@Transactional` nos use cases.
- Endpoints retornam 200 em criacao em vez de 201.
- `SenhaInvalidaException` retorna 404; autenticacao invalida deveria ser 401.
- Nao ha testes alem de `contextLoads`.

OWASP relacionado:

- A01 Broken Access Control: roles divergentes, cadastro de usuarios/cargos sem modelo administrativo claro, OS aceita usuarioId arbitrario.
- A02 Cryptographic Failures: segredo JWT hardcoded.
- A05 Security Misconfiguration: detalhes de banco expostos e Swagger publico sem perfil/ambiente.
- A07 Identification and Authentication Failures: login sem rate limiting, resposta inadequada para credenciais invalidas.
- A09 Security Logging and Monitoring Failures: uso de `System.out.println` em vez de logging estruturado; risco de logs sensiveis.

### Status após atualização da main

| Vulnerabilidade/problema original | Status atual | Observação |
|---|---|---|
| Cadastro inicial de usuario bloqueado por `/usuario` vs `/usuarios` | ✔ RESOLVIDO | `SecurityConfig` libera `POST /usuarios`. |
| Endpoints de peças inacessíveis por `ALMOXARIFADO` vs `ALMOXARIFE` | ✔ RESOLVIDO | `PecaController` usa `hasRole('ALMOXARIFE')`. |
| `VeiculoMapper.toEntity` não seta `clienteId` | ✔ RESOLVIDO | `VeiculoMapper` agora chama `setClienteId`. |
| `ClienteMapper.toEntity` não preserva `id` | ❌ AINDA PROBLEMA | Continua criando `ClienteEntity` sem `id` e sem `status`. |
| `jwt.secret` hardcoded | ❌ AINDA PROBLEMA | Permanece em `application.properties` e `docker-compose.yml`. |
| Criação pública de usuários com qualquer cargo | ❌ AINDA PROBLEMA | `POST /usuarios` é público e permite `ATENDENTE`, `MECANICO`, `ALMOXARIFE`; senha agora é mais forte, mas não há aprovação/admin. |
| Logs sensíveis no filtro JWT com `System.out.println` | ✔ RESOLVIDO | Prints removidos de `JwtAuthenticationFilter`. |
| `DataIntegrityViolationException` expõe detalhe do banco | ⚠️ PARCIALMENTE RESOLVIDO | Usa `ErrorResponse`, mas mensagem ainda concatena `ex.getCause().getMessage()`. |
| OS aceita `usuarioId` no payload e não compara com autenticado | ✔ RESOLVIDO | `OrdemServicoIncluirRequest` não tem `usuarioId`; use case usa `CurrentUserProvider`. |
| Ausência de handler para JWT inválido/expirado e `IllegalArgumentException` | ⚠️ PARCIALMENTE RESOLVIDO | Há handler genérico, mas JWT inválido no filtro pode ocorrer antes do controller advice; `IllegalArgumentException` vira 500 genérico. |
| Falta rate limiting em `/auth/login` | ❌ AINDA PROBLEMA | Nenhuma evidência de rate limiting. |
| `LoginRequest` sem Bean Validation | ❌ AINDA PROBLEMA | DTO continua sem `@NotBlank`/`@Email`. |
| `ClienteAlterarRequest` sem Bean Validation | ❌ AINDA PROBLEMA | DTO continua sem validações. |
| `VeiculoIncluirRequest.ano` usa `@Min/@Max` em `String` | ❌ AINDA PROBLEMA | Ainda presente. |
| Banco sem CHECK para `ordem_servicos.situacao` | ❌ AINDA PROBLEMA | Migration V6 permanece sem CHECK de enum. |
| Banco permite peça com valor/estoque negativos fora da API | ❌ AINDA PROBLEMA | Migration V4 permanece sem CHECK para valores não negativos. |
| `clientes.usuario_id` sem FK | ❌ AINDA PROBLEMA | Migration V2 permanece sem FK. |
| Ausência de `@Transactional` | ❌ AINDA PROBLEMA | Nenhum use case analisado possui `@Transactional`. |
| Endpoints retornam 200 em criação | ❌ AINDA PROBLEMA | Controllers continuam usando `ResponseEntity.ok(...)`. |
| `SenhaInvalidaException` retorna 404 | ✔ RESOLVIDO | Agora retorna 401 em `GlobalExceptionHandler`. |
| Apenas teste `contextLoads` | ❌ AINDA PROBLEMA | Testes continuam mínimos. |
| `servicos`, `prestacao_servicos`, `alocacao_pecas` ausentes | ✔ RESOLVIDO | Migrations e endpoints foram adicionados. |
| Novo risco: `PrestacaoServico.dataInicio` obrigatório mas não setado no cadastro | ❌ NOVO PROBLEMA | `POST /prestacao-servico` tende a violar NOT NULL em `data_inicio`. |
| Novo risco: mappers novos não preservam `id`/`status` | ❌ NOVO PROBLEMA | Afeta alterações/ativações/inativações/finalizações de serviço/prestação e possivelmente OS/alocação em fluxos futuros. |
| Novo risco: `ServicoJpaRepository.findByNome` retorna domínio em repository de entity | ❌ NOVO PROBLEMA | `JpaRepository<ServicoEntity, UUID>` declara `Optional<Servico> findByNome(String nome)`. |
| Novo risco: `CadastrarClienteUseCase` instancia CPF/CNPJ antes de null-check | ❌ NOVO PROBLEMA | PF-only/PJ-only podem falhar antes da regra esperada por null no documento oposto. |

## 12. Recomendações antes dos testes completos

1. Corrigir `SecurityConfig` para liberar ou proteger intencionalmente `POST /usuarios`; se publico, restringir cargos ou criar fluxo administrativo.
2. Padronizar role de pecas: trocar `ALMOXARIFADO` para `ALMOXARIFE` ou adicionar novo cargo no enum e no CHECK.
3. Corrigir mappers para preservar `id`, `status` e FKs necessarias, especialmente `VeiculoMapper` e `ClienteMapper`.
4. Adicionar handler padronizado para autenticacao/autorizacao, JWT invalido, `IllegalArgumentException` e `UnexpectedTypeException`.
5. Remover secrets hardcoded e usar variaveis de ambiente obrigatorias.
6. Revisar DTOs com Bean Validation completa e compativel com tipos.
7. Adicionar testes de integracao para auth, roles, constraints e principais fluxos.
8. Definir se `servicos`, `prestacao_servicos` e `alocacao_pecas` fazem parte do escopo antes de criar mock completo.

Atualização após main:

1. `servicos`, `prestacao_servicos` e `alocacao_pecas` agora fazem parte do escopo e devem entrar na V9.
2. Priorizar correção de `ClienteMapper`, `ServicoMapper`, `PrestacaoServicoMapper`, `AlocacaoPecaMapper` e `OrdemServicoMapper` para preservar `id`/`status` em updates.
3. Corrigir `PrestacaoServico` para setar `dataInicio` no cadastro ou permitir null no banco, conforme regra de negócio desejada.
4. Corrigir `ServicoJpaRepository.findByNome` para retornar `Optional<ServicoEntity>` e mapear no adapter.
5. Remover detalhes de `DataIntegrityViolationException` da resposta externa.
6. Trocar `jwt.secret`/`JWT_SECRET` hardcoded por variável obrigatória sem valor default versionado.
7. Corrigir validações de `CadastrarClienteUseCase` para não instanciar `CPF`/`CNPJ` quando o respectivo campo vier null.
8. Definir política para cadastro público de usuários e criação de cargos operacionais.

## 13. Comandos úteis

Subir banco:

```bash
docker-compose up -d postgres
```

Rodar aplicacao com wrapper Maven no Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Rodar testes:

```bash
.\mvnw.cmd test
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

Login via curl:

```bash
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d "{\"email\":\"atendente@mecanica.test\",\"senha\":\"123456\"}"
```

Header JWT para Postman/Swagger:

```http
Authorization: Bearer <token>
```
