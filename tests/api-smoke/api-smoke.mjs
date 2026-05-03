const baseUrl = "http://localhost:8080";

const credentials = {
  atendente: { email: "atendente@mecanica.test", senha: "Senha@123" },
  mecanico: { email: "mecanico@mecanica.test", senha: "Senha@123" },
  almoxarife: { email: "almoxarife@mecanica.test", senha: "Senha@123" },
};

const v9 = {
  atendenteId: "11111111-1111-1111-1111-111111111111",
  clientePfId: "33333333-3333-3333-3333-333333333333",
  clientePfCpf: "52998224725",
  veiculoId: "44444444-4444-4444-4444-444444444444",
  pecaId: "55555555-5555-5555-5555-555555555555",
  servicoId: "77777777-7777-7777-7777-777777777777",
  ordemServicoId: "66666666-6666-6666-6666-666666666666",
  prestacaoServicoId: "88888888-8888-8888-8888-888888888888",
};

const missingUuid = "00000000-0000-0000-0000-000000000001";
const runId = Date.now();
const failures = [];

const state = {
  tokenAtendente: null,
  tokenMecanico: null,
  tokenAlmoxarife: null,
  clientePf: null,
  clientePj: null,
  veiculo: null,
  servico: null,
  ordemServico: null,
  prestacaoServico: null,
  peca: null,
};

function pass(name) {
  console.log(`✅ PASS - ${name}`);
}

function fail(name, expected, received, body) {
  const renderedBody = typeof body === "string" ? body : JSON.stringify(body);
  const entry = { name, expected, received, body: renderedBody };
  failures.push(entry);
  console.log(`❌ FAIL - ${name} - esperado ${expected} vs recebido ${received} - ${renderedBody}`);
}

async function parseBody(response) {
  const text = await response.text();
  if (!text) return null;

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

async function request(method, path, { token, body } = {}) {
  const headers = { Accept: "application/json" };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  if (body !== undefined) {
    headers["Content-Type"] = "application/json";
  }

  const response = await fetch(`${baseUrl}${path}`, {
    method,
    headers,
    body: body === undefined ? undefined : JSON.stringify(body),
  });

  return {
    status: response.status,
    body: await parseBody(response),
  };
}

function expectStatus(name, result, expectedStatuses) {
  const expected = Array.isArray(expectedStatuses) ? expectedStatuses : [expectedStatuses];

  if (expected.includes(result.status)) {
    pass(name);
    return true;
  }

  fail(name, expected.join(" ou "), result.status, result.body);
  return false;
}

async function test(name, expectedStatuses, operation) {
  try {
    const result = await operation();
    expectStatus(name, result, expectedStatuses);
    return result;
  } catch (error) {
    fail(name, "requisição executada", "erro local", error.message);
    return { status: 0, body: null };
  }
}

async function login(label, credential) {
  const result = await request("POST", "/auth/login", { body: credential });
  if (!expectStatus(`AUTH - login ${label}`, result, 200)) {
    throw new Error(`Login falhou para ${label}. Verifique se a aplicação está rodando e se a V9 foi aplicada.`);
  }

  if (!result.body?.token) {
    fail(`AUTH - token ${label}`, "body.token preenchido", "body.token ausente", result.body);
    throw new Error(`Token ausente para ${label}.`);
  }

  return result.body.token;
}

function onlyDigits(value) {
  return String(value).replace(/\D/g, "");
}

function cpfFromSeed(seed) {
  const base = onlyDigits(seed).padStart(9, "0").slice(-9);
  const firstDigit = cpfDigit(base, 10);
  const secondDigit = cpfDigit(`${base}${firstDigit}`, 11);
  return `${base}${firstDigit}${secondDigit}`;
}

function cpfDigit(value, weightStart) {
  const sum = value
    .split("")
    .reduce((total, digit, index) => total + Number(digit) * (weightStart - index), 0);
  const digit = 11 - (sum % 11);
  return digit >= 10 ? 0 : digit;
}

function cnpjFromSeed(seed) {
  const base = onlyDigits(seed).padStart(12, "0").slice(-12);
  const firstDigit = cnpjDigit(base, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
  const secondDigit = cnpjDigit(`${base}${firstDigit}`, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
  return `${base}${firstDigit}${secondDigit}`;
}

function cnpjDigit(value, weights) {
  const sum = value
    .split("")
    .reduce((total, character, index) => total + Number(character) * weights[index], 0);
  const mod = sum % 11;
  return mod < 2 ? 0 : 11 - mod;
}

function uniquePlate(offset = 0) {
  const value = (runId + offset) % 10000;
  return `TST${String(value).padStart(4, "0")}`;
}

function assertHasId(name, resource) {
  if (!resource?.id) {
    fail(name, "body.id preenchido", "body.id ausente", resource);
    return false;
  }

  return true;
}

async function runPublicTests() {
  const result = await test("PUBLIC - POST /usuarios", 200, () =>
    request("POST", "/usuarios", {
      body: {
        nome: `Usuario Smoke ${runId}`,
        email: `usuario.smoke.${runId}@mecanica.test`,
        senha: "Senha@123",
        cargoEnum: "ATENDENTE",
      },
    }),
  );

  assertHasId("PUBLIC - usuário criado com id", result.body);
}

async function runAtendenteTests() {
  const token = state.tokenAtendente;
  const cpf = cpfFromSeed(runId);
  const cnpj = cnpjFromSeed(runId + 1000000);
  const placa = uniquePlate(1);

  const criarPf = await test("ATENDENTE - POST /clientes PF válido", 200, () =>
    request("POST", "/clientes", {
      token,
      body: {
        nome: `Cliente PF Smoke ${runId}`,
        cpf,
        cnpj: null,
        email: `cliente.pf.${runId}@mecanica.test`,
        telefone: "11987654321",
      },
    }),
  );
  state.clientePf = criarPf.body;
  assertHasId("ATENDENTE - cliente PF criado com id", state.clientePf);

  const criarPj = await test("ATENDENTE - POST /clientes PJ válido", 200, () =>
    request("POST", "/clientes", {
      token,
      body: {
        nome: `Cliente PJ Smoke ${runId}`,
        cpf: null,
        cnpj,
        email: `cliente.pj.${runId}@mecanica.test`,
        telefone: "1133334444",
      },
    }),
  );
  state.clientePj = criarPj.body;
  assertHasId("ATENDENTE - cliente PJ criado com id", state.clientePj);

  await test("ATENDENTE - PATCH /clientes/{id}", 200, () =>
    request("PATCH", `/clientes/${state.clientePf?.id}`, {
      token,
      body: {
        nome: `Cliente PF Smoke Alterado ${runId}`,
        telefone: "11999998888",
      },
    }),
  );

  await test("ATENDENTE - GET /clientes/por-cpf/{cpf}", 200, () =>
    request("GET", `/clientes/por-cpf/${cpf}`, { token }),
  );

  await test("ATENDENTE - GET /clientes/por-cnpj/{cnpj}", 200, () =>
    request("GET", `/clientes/por-cnpj/${cnpj}`, { token }),
  );

  await test("ATENDENTE - GET /clientes/por-usuario/{usuarioId}", 200, () =>
    request("GET", `/clientes/por-usuario/${v9.atendenteId}`, { token }),
  );

  const criarVeiculo = await test("ATENDENTE - POST /veiculos válido", 200, () =>
    request("POST", "/veiculos", {
      token,
      body: {
        placa,
        marca: "Toyota",
        modelo: "Corolla Smoke",
        ano: "2021",
        quantidadeEixos: 2,
        clienteId: state.clientePf?.id,
      },
    }),
  );
  state.veiculo = criarVeiculo.body;
  assertHasId("ATENDENTE - veículo criado com id", state.veiculo);

  await test("ATENDENTE - GET /veiculos/{id}", 200, () =>
    request("GET", `/veiculos/${state.veiculo?.id}`, { token }),
  );

  await test("ATENDENTE - GET /veiculos/por-placa/{placa}", 200, () =>
    request("GET", `/veiculos/por-placa/${placa}`, { token }),
  );

  await test("ATENDENTE - GET /veiculos/por-cliente/{clienteId}", 200, () =>
    request("GET", `/veiculos/por-cliente/${state.clientePf?.id}`, { token }),
  );
}

async function runMecanicoTests() {
  const token = state.tokenMecanico;

  const criarServico = await test("MECANICO - POST /servicos", 200, () =>
    request("POST", "/servicos", {
      token,
      body: {
        nome: `Servico Smoke ${runId}`,
        descricao: "Servico criado pela suite smoke",
      },
    }),
  );
  state.servico = criarServico.body;
  assertHasId("MECANICO - serviço criado com id", state.servico);

  await test("MECANICO - PUT /servicos/{id}", 200, () =>
    request("PUT", `/servicos/${state.servico?.id}`, {
      token,
      body: {
        nome: `Servico Smoke Alterado ${runId}`,
        descricao: "Servico alterado pela suite smoke",
      },
    }),
  );

  await test("MECANICO - PATCH /servicos/{id}/ativar", 200, () =>
    request("PATCH", `/servicos/${state.servico?.id}/ativar`, { token }),
  );

  await test("MECANICO - PATCH /servicos/{id}/inativar", 200, () =>
    request("PATCH", `/servicos/${state.servico?.id}/inativar`, { token }),
  );

  await test("MECANICO - reativar serviço para fluxo de prestação", 200, () =>
    request("PATCH", `/servicos/${state.servico?.id}/ativar`, { token }),
  );

  const criarOrdem = await test("MECANICO - POST /ordem-servicos", 200, () =>
    request("POST", "/ordem-servicos", {
      token,
      body: {
        observacao: `OS Smoke ${runId}`,
        veiculoId: state.veiculo?.id ?? v9.veiculoId,
      },
    }),
  );
  state.ordemServico = criarOrdem.body;
  assertHasId("MECANICO - ordem de serviço criada com id", state.ordemServico);

  await test("MECANICO - GET /ordem-servicos/{id}", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}`, { token }),
  );

  await test("MECANICO - GET /ordem-servicos/veiculo/placa/{placa}", 200, () =>
    request("GET", `/ordem-servicos/veiculo/placa/${state.veiculo?.placa ?? "ABC1D23"}`, { token })
  );

  await test("MECANICO - GET /ordem-servicos/{id}/tempo-medio", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}/tempo-medio`, { token }),
  );

  const criarPrestacao = await test("MECANICO - POST /prestacao-servico", 200, () =>
    request("POST", "/prestacao-servico", {
      token,
      body: {
        precoMaoDeObra: 180.5,
        ordemServicoId: state.ordemServico?.id,
        servicoId: state.servico?.id,
      },
    }),
  );
  state.prestacaoServico = criarPrestacao.body;
  assertHasId("MECANICO - prestação de serviço criada com id", state.prestacaoServico);

  await test("MECANICO - GET /prestacao-servico/por-ordem-servico/{ordemServicoId}", 200, () =>
    request("GET", `/prestacao-servico/por-ordem-servico/${state.ordemServico?.id}`, { token }),
  );

  await test("MECANICO - enviar OS para aprovação", 200, () =>
    request("PATCH", `/ordem-servicos/${state.ordemServico?.id}/enviar-para-aguardar-aprovacao`, { token }),
  );

  await test("MECANICO - iniciar execução da OS", 200, () =>
    request("PATCH", `/ordem-servicos/${state.ordemServico?.id}/iniciar-execucao`, { token }),
  );

  await test("MECANICO - PATCH /prestacao-servico/{id}/finalizar", 200, () =>
    request("PATCH", `/prestacao-servico/${state.prestacaoServico?.id}/finalizar`, { token }),
  );
}

async function runAlmoxarifeTests() {
  const token = state.tokenAlmoxarife;

  const criarPeca = await test("ALMOXARIFE - POST /pecas", 200, () =>
    request("POST", "/pecas", {
      token,
      body: {
        nome: `Peca Smoke ${runId}`,
        marca: "Bosch",
        valorUnitario: 59.9,
        quantidadeEstoque: 10,
      },
    }),
  );
  state.peca = criarPeca.body;
  assertHasId("ALMOXARIFE - peça criada com id", state.peca);

  await test("ALMOXARIFE - PUT /pecas/{id}", 200, () =>
    request("PUT", `/pecas/${state.peca?.id}`, {
      token,
      body: {
        nome: `Peca Smoke Alterada ${runId}`,
        marca: "Fras-le",
        valorUnitario: 69.9,
        quantidadeEstoque: 15,
      },
    }),
  );

  await test("ALMOXARIFE - GET /pecas/{id}", 200, () =>
    request("GET", `/pecas/${state.peca?.id}`, { token }),
  );

  await test("ALMOXARIFE - POST /alocacao-pecas", 200, () =>
    request("POST", "/alocacao-pecas", {
      token,
      body: {
        quantidadeNecessaria: 1,
        prestacaoServicoId: state.prestacaoServico?.id ?? v9.prestacaoServicoId,
        pecaId: state.peca?.id,
      },
    }),
  );
}

async function runSecurityTests() {
  await test("SECURITY - endpoint protegido sem token deve retornar 401 ou 403", [401, 403], () =>
    request("GET", `/pecas/${v9.pecaId}`),
  );

  await test("SECURITY - /clientes com MECANICO deve retornar 403", 403, () =>
    request("GET", `/clientes/por-cpf/${v9.clientePfCpf}`, { token: state.tokenMecanico }),
  );

  await test("SECURITY - /veiculos com MECANICO deve retornar 403", 403, () =>
    request("GET", `/veiculos/${v9.veiculoId}`, { token: state.tokenMecanico }),
  );

  await test("SECURITY - /servicos com ATENDENTE deve retornar 403", 403, () =>
    request("POST", "/servicos", {
      token: state.tokenAtendente,
      body: {
        nome: `Servico Bloqueado ${runId}`,
        descricao: "Nao deve ser criado",
      },
    }),
  );

  await test("SECURITY - /ordem-servicos com ATENDENTE deve retornar 403", 403, () =>
    request("POST", "/ordem-servicos", {
      token: state.tokenAtendente,
      body: {
        observacao: "Nao deve ser criada",
        veiculoId: v9.veiculoId,
      },
    }),
  );

  await test("SECURITY - /pecas com ATENDENTE deve retornar 403", 403, () =>
    request("GET", `/pecas/${v9.pecaId}`, { token: state.tokenAtendente }),
  );

  await test("SECURITY - /alocacao-pecas com MECANICO deve retornar 403", 403, () =>
    request("POST", "/alocacao-pecas", {
      token: state.tokenMecanico,
      body: {
        quantidadeNecessaria: 1,
        prestacaoServicoId: v9.prestacaoServicoId,
        pecaId: v9.pecaId,
      },
    }),
  );
}

async function runValidationTests() {
  await test("VALIDATION - POST /veiculos com placa inválida deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/veiculos", {
      token: state.tokenAtendente,
      body: {
        placa: "INVALIDA",
        marca: "Toyota",
        modelo: "Corolla",
        ano: "2021",
        quantidadeEixos: 2,
        clienteId: state.clientePf?.id ?? v9.clientePfId,
      },
    }),
  );

  await test("VALIDATION - POST /veiculos com placa duplicada deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/veiculos", {
      token: state.tokenAtendente,
      body: {
        placa: state.veiculo?.placa ?? "ABC1D23",
        marca: "Toyota",
        modelo: "Corolla",
        ano: "2021",
        quantidadeEixos: 2,
        clienteId: state.clientePf?.id ?? v9.clientePfId,
      },
    }),
  );

  await test("VALIDATION - POST /clientes sem CPF e sem CNPJ deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/clientes", {
      token: state.tokenAtendente,
      body: {
        nome: `Cliente Documento Ausente ${runId}`,
        cpf: null,
        cnpj: null,
        email: `cliente.sem-doc.${runId}@mecanica.test`,
        telefone: "11987654321",
      },
    }),
  );

  await test("VALIDATION - POST /clientes com CPF e CNPJ juntos deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/clientes", {
      token: state.tokenAtendente,
      body: {
        nome: `Cliente Documento Duplicado ${runId}`,
        cpf: cpfFromSeed(runId + 2000000),
        cnpj: cnpjFromSeed(runId + 3000000),
        email: `cliente.docs.${runId}@mecanica.test`,
        telefone: "11987654321",
      },
    }),
  );

  await test("VALIDATION - POST /pecas com valorUnitario negativo deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/pecas", {
      token: state.tokenAlmoxarife,
      body: {
        nome: `Peca Valor Invalido ${runId}`,
        marca: "Bosch",
        valorUnitario: -1,
        quantidadeEstoque: 1,
      },
    }),
  );

  await test("VALIDATION - POST /pecas com quantidadeEstoque negativa deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/pecas", {
      token: state.tokenAlmoxarife,
      body: {
        nome: `Peca Estoque Invalido ${runId}`,
        marca: "Bosch",
        valorUnitario: 10,
        quantidadeEstoque: -1,
      },
    }),
  );

  await test("VALIDATION - POST /servicos com nome duplicado deve retornar 400 ou 422", [400, 422], () =>
    request("POST", "/servicos", {
      token: state.tokenMecanico,
      body: {
        nome: `Servico Smoke Alterado ${runId}`,
        descricao: "Duplicado pela suite smoke",
      },
    }),
  );

  await test("VALIDATION - GET /veiculos/{id} com UUID inexistente deve retornar 404", 404, () =>
    request("GET", `/veiculos/${missingUuid}`, { token: state.tokenAtendente }),
  );

  await test("VALIDATION - GET /pecas/{id} com UUID inexistente deve retornar 404", 404, () =>
    request("GET", `/pecas/${missingUuid}`, { token: state.tokenAlmoxarife }),
  );

  await test("VALIDATION - GET /ordem-servicos/{id} com UUID inexistente deve retornar 404", 404, () =>
    request("GET", `/ordem-servicos/${missingUuid}`, { token: state.tokenMecanico }),
  );
}

function printSummary() {
  if (failures.length === 0) {
    console.log("\nResultado: todos os testes passaram.");
    return;
  }

  console.log(`\nResultado: ${failures.length} falha(s).`);
  console.log("Bugs/achados funcionais:");
  for (const failure of failures) {
    console.log(`- ${failure.name}: esperado ${failure.expected}, recebido ${failure.received}`);
  }
}

async function main() {
  console.log(`API smoke tests - ${baseUrl}`);
  console.log(`Run ID: ${runId}`);

  await runPublicTests();

  state.tokenAtendente = await login("ATENDENTE", credentials.atendente);
  state.tokenMecanico = await login("MECANICO", credentials.mecanico);
  state.tokenAlmoxarife = await login("ALMOXARIFE", credentials.almoxarife);

  await runAtendenteTests();
  await runMecanicoTests();
  await runAlmoxarifeTests();
  await runSecurityTests();
  await runValidationTests();

  printSummary();
  process.exitCode = failures.length > 0 ? 1 : 0;
}

main().catch((error) => {
  fail("execução da suíte", "concluir execução", "erro fatal", error.message);
  printSummary();
  process.exitCode = 1;
});
