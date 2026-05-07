const baseUrl = process.env.API_BASE_URL ?? "http://localhost:8080";

const credentials = {
  atendente: { email: `atendente.smoke.${Date.now()}@mecanica.test`, senha: "Senha@123" },
  mecanico: { email: `mecanico.smoke.${Date.now()}@mecanica.test`, senha: "Senha@123" },
  almoxarife: { email: `almoxarife.smoke.${Date.now()}@mecanica.test`, senha: "Senha@123" },
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
  usuarioAtendente: null,
  usuarioMecanico: null,
  usuarioAlmoxarife: null,
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

  if (token) headers.Authorization = `Bearer ${token}`;
  if (body !== undefined) headers["Content-Type"] = "application/json";

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
    fail(name, "requisicao executada", "erro local", error.message);
    return { status: 0, body: null };
  }
}

function assertHasId(name, resource) {
  if (resource?.id) {
    pass(name);
    return true;
  }

  fail(name, "body.id preenchido", "body.id ausente", resource);
  return false;
}

function assertPresent(name, value) {
  if (value !== null && value !== undefined && value !== "") {
    pass(name);
    return true;
  }

  fail(name, "valor preenchido", value, { value });
  return false;
}

function assertEquals(name, actual, expected) {
  if (actual === expected) {
    pass(name);
    return true;
  }

  fail(name, expected, actual, { actual, expected });
  return false;
}

function decimal(value) {
  return Number.parseFloat(value);
}

function assertDecimalEquals(name, actual, expected, precision = 2) {
  const factor = 10 ** precision;
  const normalizedActual = Math.round(decimal(actual) * factor) / factor;
  const normalizedExpected = Math.round(decimal(expected) * factor) / factor;

  if (normalizedActual === normalizedExpected) {
    pass(name);
    return true;
  }

  fail(name, normalizedExpected, normalizedActual, { actual, expected });
  return false;
}

async function login(label, credential) {
  const result = await request("POST", "/auth/login", { body: credential });
  if (!expectStatus(`AUTH - login ${label}`, result, 200)) {
    throw new Error(`Login falhou para ${label}. Verifique se a aplicacao esta rodando e se a V9 foi aplicada.`);
  }

  if (!result.body?.jwt) {
    fail(`AUTH - token ${label}`, "body.token preenchido", "body.token ausente", result.body);
    throw new Error(`Token ausente para ${label}.`);
  }

  return result.body.jwt;
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

async function criarServicoSmoke(nomeSufixo) {
  const result = await test(`MECANICO - POST /servicos para ${nomeSufixo}`, 200, () =>
    request("POST", "/servicos", {
      token: state.tokenMecanico,
      body: {
        nome: `Servico ${nomeSufixo} ${runId}`,
        descricao: `Servico ${nomeSufixo} criado pela suite smoke`,
      },
    }),
  );
  assertHasId(`MECANICO - servico ${nomeSufixo} criado com id`, result.body);
  return result.body;
}

async function criarOrdemSmoke(nomeSufixo) {
  const result = await test(`MECANICO - POST /ordem-servicos para ${nomeSufixo}`, 200, () =>
    request("POST", "/ordem-servicos", {
      token: state.tokenMecanico,
      body: {
        observacao: `OS ${nomeSufixo} Smoke ${runId}`,
        veiculoId: state.veiculo?.id ?? v9.veiculoId,
      },
    }),
  );
  assertHasId(`MECANICO - OS ${nomeSufixo} criada com id`, result.body);
  return result.body;
}

async function criarPrestacaoSmoke(nomeSufixo, ordemServicoId, servicoId, precoMaoDeObra = 120.5) {
  const result = await test(`MECANICO - POST /prestacao-servico para ${nomeSufixo}`, 200, () =>
    request("POST", "/prestacao-servico", {
      token: state.tokenMecanico,
      body: {
        precoMaoDeObra,
        ordemServicoId,
        servicoId,
      },
    }),
  );
  assertHasId(`MECANICO - prestacao ${nomeSufixo} criada com id`, result.body);
  return result.body;
}

async function criarPecaSmoke(nomeSufixo, valorUnitario = 45.9, quantidadeEstoque = 8) {
  const result = await test(`ALMOXARIFE - POST /pecas para ${nomeSufixo}`, 200, () =>
    request("POST", "/pecas", {
      token: state.tokenAlmoxarife,
      body: {
        nome: `Peca ${nomeSufixo} ${runId}`,
        marca: "Bosch",
        valorUnitario,
        quantidadeEstoque,
      },
    }),
  );
  assertHasId(`ALMOXARIFE - peca ${nomeSufixo} criada com id`, result.body);
  return result.body;
}

async function runPublicTests() {
  const atendente = await test("PUBLIC - POST /usuarios ATENDENTE", 200, () =>
    request("POST", "/usuarios", {
      body: {
        nome: `Atendente Smoke ${runId}`,
        email: credentials.atendente.email,
        senha: credentials.atendente.senha,
        cargoEnum: "ATENDENTE",
      },
    }),
  );
  state.usuarioAtendente = atendente.body;
  assertHasId("PUBLIC - usuario ATENDENTE criado com id", state.usuarioAtendente);

  const mecanico = await test("PUBLIC - POST /usuarios MECANICO", 200, () =>
    request("POST", "/usuarios", {
      body: {
        nome: `Mecanico Smoke ${runId}`,
        email: credentials.mecanico.email,
        senha: credentials.mecanico.senha,
        cargoEnum: "MECANICO",
      },
    }),
  );
  state.usuarioMecanico = mecanico.body;
  assertHasId("PUBLIC - usuario MECANICO criado com id", state.usuarioMecanico);

  const almoxarife = await test("PUBLIC - POST /usuarios ALMOXARIFE", 200, () =>
    request("POST", "/usuarios", {
      body: {
        nome: `Almoxarife Smoke ${runId}`,
        email: credentials.almoxarife.email,
        senha: credentials.almoxarife.senha,
        cargoEnum: "ALMOXARIFE",
      },
    }),
  );
  state.usuarioAlmoxarife = almoxarife.body;
  assertHasId("PUBLIC - usuario ALMOXARIFE criado com id", state.usuarioAlmoxarife);
}

async function runAtendenteTests() {
  const token = state.tokenAtendente;
  const cpf = cpfFromSeed(runId);
  const cnpj = cnpjFromSeed(runId + 1000000);
  const placa = uniquePlate(1);

  const criarPf = await test("ATENDENTE - POST /clientes PF valido", 200, () =>
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

  const criarPj = await test("ATENDENTE - POST /clientes PJ valido", 200, () =>
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
    request("GET", `/clientes/por-usuario/${state.usuarioAtendente?.id ?? v9.atendenteId}`, { token }),
  );

  const criarVeiculo = await test("ATENDENTE - POST /veiculos valido", 200, () =>
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
  assertHasId("ATENDENTE - veiculo criado com id", state.veiculo);

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

async function runMecanicoSetupTests() {
  const token = state.tokenMecanico;
  const precoMaoDeObra = 180.5;

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
  assertHasId("MECANICO - servico criado com id", state.servico);

  const alterarServico = await test("MECANICO - PUT /servicos/{id}", 200, () =>
    request("PUT", `/servicos/${state.servico?.id}`, {
      token,
      body: {
        nome: `Servico Smoke Alterado ${runId}`,
        descricao: "Servico alterado pela suite smoke",
      },
    }),
  );
  state.servico = alterarServico.body;

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
  assertHasId("MECANICO - ordem de servico criada com id", state.ordemServico);
  assertEquals("MECANICO - OS criada como RECEBIDA", state.ordemServico?.situacao, "RECEBIDA");
  assertDecimalEquals("MECANICO - OS criada com valorTotal zerado", state.ordemServico?.valorTotal, 0);

  const buscarOrdem = await test("MECANICO - GET /ordem-servicos/{id}", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}`, { token }),
  );
  state.ordemServico = buscarOrdem.body;

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
        precoMaoDeObra,
        ordemServicoId: state.ordemServico?.id,
        servicoId: state.servico?.id,
      },
    }),
  );
  state.prestacaoServico = criarPrestacao.body;
  assertHasId("MECANICO - prestacao de servico criada com id", state.prestacaoServico);
  assertDecimalEquals("MECANICO - prestacao criada com subtotal da mao de obra", state.prestacaoServico?.subtotal, precoMaoDeObra);

  const prestacoes = await test("MECANICO - GET /prestacao-servico/por-ordem-servico/{ordemServicoId}", 200, () =>
    request("GET", `/prestacao-servico/por-ordem-servico/${state.ordemServico?.id}`, { token }),
  );

  const prestacaoListada = Array.isArray(prestacoes.body)
    ? prestacoes.body.find((item) => item.id === state.prestacaoServico?.id)
    : null;
  assertPresent("MECANICO - prestacao criada aparece na listagem da OS", prestacaoListada?.id);

  const ordemAposPrestacao = await test("MECANICO - GET /ordem-servicos/{id} apos criar prestacao", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}`, { token }),
  );
  state.ordemServico = ordemAposPrestacao.body;
  assertEquals("MECANICO - criar prestacao move OS para EM_DIAGNOSTICO", state.ordemServico?.situacao, "EM_DIAGNOSTICO");
  assertPresent("MECANICO - criar prestacao preenche dataDiagnostico", state.ordemServico?.dataDiagnostico);
  assertDecimalEquals("MECANICO - criar prestacao soma mao de obra no valorTotal da OS", state.ordemServico?.valorTotal, precoMaoDeObra);
}

async function runAlmoxarifeTests() {
  const token = state.tokenAlmoxarife;
  const quantidadeEstoqueInicial = 10;
  const quantidadeEstoqueAtualizada = 15;
  const valorUnitarioAtualizado = 69.9;
  const quantidadeAlocada = 2;
  const precoMaoDeObra = decimal(state.prestacaoServico?.precoMaoDeObra);
  const valorPecas = valorUnitarioAtualizado * quantidadeAlocada;
  const subtotalEsperado = precoMaoDeObra + valorPecas;

  const criarPeca = await test("ALMOXARIFE - POST /pecas", 200, () =>
    request("POST", "/pecas", {
      token,
      body: {
        nome: `Peca Smoke ${runId}`,
        marca: "Bosch",
        valorUnitario: 59.9,
        quantidadeEstoque: quantidadeEstoqueInicial,
      },
    }),
  );
  state.peca = criarPeca.body;
  assertHasId("ALMOXARIFE - peca criada com id", state.peca);
  assertEquals("ALMOXARIFE - peca criada com estoque inicial", state.peca?.quantidadeEstoque, quantidadeEstoqueInicial);

  const atualizarPeca = await test("ALMOXARIFE - PUT /pecas/{id}", 200, () =>
    request("PUT", `/pecas/${state.peca?.id}`, {
      token,
      body: {
        nome: `Peca Smoke Alterada ${runId}`,
        marca: "Fras-le",
        valorUnitario: valorUnitarioAtualizado,
        quantidadeEstoque: quantidadeEstoqueAtualizada,
      },
    }),
  );
  state.peca = atualizarPeca.body;
  assertDecimalEquals("ALMOXARIFE - peca atualizada com valorUnitario esperado", state.peca?.valorUnitario, valorUnitarioAtualizado);
  assertEquals("ALMOXARIFE - peca atualizada com estoque esperado", state.peca?.quantidadeEstoque, quantidadeEstoqueAtualizada);

  const buscarPeca = await test("ALMOXARIFE - GET /pecas/{id}", 200, () =>
    request("GET", `/pecas/${state.peca?.id}`, { token }),
  );
  state.peca = buscarPeca.body;

  const alocacao = await test("ALMOXARIFE - POST /alocacao-pecas", 200, () =>
    request("POST", "/alocacao-pecas", {
      token,
      body: {
        quantidadeNecessaria: quantidadeAlocada,
        prestacaoServicoId: state.prestacaoServico?.id ?? v9.prestacaoServicoId,
        pecaId: state.peca?.id,
      },
    }),
  );
  assertHasId("ALMOXARIFE - alocacao de peca criada com id", alocacao.body);

  const pecaAposAlocacao = await test("ALMOXARIFE - GET /pecas/{id} apos alocacao", 200, () =>
    request("GET", `/pecas/${state.peca?.id}`, { token }),
  );
  state.peca = pecaAposAlocacao.body;
  assertEquals(
    "ALMOXARIFE - alocar peca baixa quantidade do estoque",
    state.peca?.quantidadeEstoque,
    quantidadeEstoqueAtualizada - quantidadeAlocada,
  );

  const prestacoesAposAlocacao = await test("MECANICO - GET prestacoes apos alocar peca", 200, () =>
    request("GET", `/prestacao-servico/por-ordem-servico/${state.ordemServico?.id}`, { token: state.tokenMecanico }),
  );
  const prestacaoAposAlocacao = Array.isArray(prestacoesAposAlocacao.body)
    ? prestacoesAposAlocacao.body.find((item) => item.id === state.prestacaoServico?.id)
    : null;
  state.prestacaoServico = prestacaoAposAlocacao ?? state.prestacaoServico;
  assertDecimalEquals("MECANICO - alocar peca soma valor no subtotal da prestacao", state.prestacaoServico?.subtotal, subtotalEsperado);

  const ordemAposAlocacao = await test("MECANICO - GET /ordem-servicos/{id} apos alocar peca", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}`, { token: state.tokenMecanico }),
  );
  state.ordemServico = ordemAposAlocacao.body;
  assertDecimalEquals("MECANICO - alocar peca soma valor no valorTotal da OS", state.ordemServico?.valorTotal, subtotalEsperado);
}

async function runOrdemServicoExecutionFlowTests() {
  const token = state.tokenMecanico;

  const aguardandoAprovacao = await test("MECANICO - PATCH /ordem-servicos/{id}/enviar-para-aguardar-aprovacao", 200, () =>
    request("PATCH", `/ordem-servicos/${state.ordemServico?.id}/enviar-para-aguardar-aprovacao`, { token }),
  );
  state.ordemServico = aguardandoAprovacao.body;
  assertEquals("MECANICO - OS enviada para AGUARDANDO_APROVACAO", state.ordemServico?.situacao, "AGUARDANDO_APROVACAO");
  assertPresent("MECANICO - OS enviada para aprovacao preenche dataAguardandoAprovacao", state.ordemServico?.dataAguardandoAprovacao);

  const emExecucao = await test("MECANICO - PATCH /ordem-servicos/{id}/iniciar-execucao", 200, () =>
    request("PATCH", `/ordem-servicos/${state.ordemServico?.id}/iniciar-execucao`, { token }),
  );
  state.ordemServico = emExecucao.body;
  assertEquals("MECANICO - OS iniciada como EM_EXECUCAO", state.ordemServico?.situacao, "EM_EXECUCAO");
  assertEquals("MECANICO - iniciar execucao marca OS como nao paga", state.ordemServico?.pago, false);
  assertPresent("MECANICO - iniciar execucao preenche dataExecucao", state.ordemServico?.dataExecucao);

  const prestacoesIniciadas = await test("MECANICO - GET prestacoes apos iniciar execucao", 200, () =>
    request("GET", `/prestacao-servico/por-ordem-servico/${state.ordemServico?.id}`, { token }),
  );
  const prestacaoIniciada = Array.isArray(prestacoesIniciadas.body)
    ? prestacoesIniciadas.body.find((item) => item.id === state.prestacaoServico?.id)
    : null;
  assertPresent("MECANICO - iniciar execucao preenche dataInicio da prestacao", prestacaoIniciada?.dataInicio);

  const finalizarPrestacao = await test("MECANICO - PATCH /prestacao-servico/{id}/finalizar", 200, () =>
    request("PATCH", `/prestacao-servico/${state.prestacaoServico?.id}/finalizar`, { token }),
  );
  state.prestacaoServico = finalizarPrestacao.body;
  assertPresent("MECANICO - finalizar prestacao preenche dataFim", state.prestacaoServico?.dataFim);

  const ordemFinalizada = await test("MECANICO - GET /ordem-servicos/{id} apos finalizar prestacao", 200, () =>
    request("GET", `/ordem-servicos/${state.ordemServico?.id}`, { token }),
  );
  state.ordemServico = ordemFinalizada.body;
  assertEquals("MECANICO - finalizar todas as prestacoes finaliza a OS", state.ordemServico?.situacao, "FINALIZADA");
  assertPresent("MECANICO - OS finalizada preenche dataFinalizada", state.ordemServico?.dataFinalizada);

  const ordemEntregue = await test("MECANICO - PATCH /ordem-servicos/{id}/pagar", 200, () =>
    request("PATCH", `/ordem-servicos/${state.ordemServico?.id}/pagar`, { token }),
  );
  state.ordemServico = ordemEntregue.body;
  assertEquals("MECANICO - pagar entrega a OS", state.ordemServico?.situacao, "ENTREGUE");
  assertEquals("MECANICO - pagar marca OS como paga", state.ordemServico?.pago, true);
  assertPresent("MECANICO - entregar preenche dataEntregue", state.ordemServico?.dataEntregue);
}

async function runRemainingEndpointTests() {
  const tokenMecanico = state.tokenMecanico;
  const tokenAlmoxarife = state.tokenAlmoxarife;

  const servicoStatus = await criarServicoSmoke("Status");

  const servicoInativo = await test("MECANICO - PATCH /servicos/{id}/inativar", 200, () =>
    request("PATCH", `/servicos/${servicoStatus?.id}/inativar`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - inativar servico altera status para INATIVO", servicoInativo.body?.statusRecursoEnum, "INATIVO");

  const servicoAtivo = await test("MECANICO - PATCH /servicos/{id}/ativar", 200, () =>
    request("PATCH", `/servicos/${servicoStatus?.id}/ativar`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - ativar servico altera status para ATIVO", servicoAtivo.body?.statusRecursoEnum, "ATIVO");

  const servicoVoltarDiagnostico = await criarServicoSmoke("VoltarDiagnostico");
  const osVoltarDiagnostico = await criarOrdemSmoke("VoltarDiagnostico");
  await criarPrestacaoSmoke("VoltarDiagnostico", osVoltarDiagnostico?.id, servicoVoltarDiagnostico?.id);

  const osAguardando = await test("MECANICO - enviar OS auxiliar para aprovacao", 200, () =>
    request("PATCH", `/ordem-servicos/${osVoltarDiagnostico?.id}/enviar-para-aguardar-aprovacao`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - OS auxiliar enviada para AGUARDANDO_APROVACAO", osAguardando.body?.situacao, "AGUARDANDO_APROVACAO");

  const osDiagnostico = await test("MECANICO - PATCH /ordem-servicos/{id}/voltar-diagnostico", 200, () =>
    request("PATCH", `/ordem-servicos/${osVoltarDiagnostico?.id}/voltar-diagnostico`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - voltar diagnostico altera OS para EM_DIAGNOSTICO", osDiagnostico.body?.situacao, "EM_DIAGNOSTICO");

  const servicoCancelar = await criarServicoSmoke("Cancelar");
  const osCancelar = await criarOrdemSmoke("Cancelar");
  await criarPrestacaoSmoke("Cancelar", osCancelar?.id, servicoCancelar?.id);

  await test("MECANICO - enviar OS cancelamento para aprovacao", 200, () =>
    request("PATCH", `/ordem-servicos/${osCancelar?.id}/enviar-para-aguardar-aprovacao`, { token: tokenMecanico }),
  );

  const osCancelada = await test("MECANICO - PATCH /ordem-servicos/{id}/cancelar", 200, () =>
    request("PATCH", `/ordem-servicos/${osCancelar?.id}/cancelar`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - cancelar OS altera status para INATIVO", osCancelada.body?.status, "INATIVO");

  const servicoInativarAlocacao = await criarServicoSmoke("InativarAlocacao");
  const osInativarAlocacao = await criarOrdemSmoke("InativarAlocacao");
  const prestacaoInativarAlocacao = await criarPrestacaoSmoke("InativarAlocacao", osInativarAlocacao?.id, servicoInativarAlocacao?.id);
  const pecaInativarAlocacao = await criarPecaSmoke("InativarAlocacao", 33.3, 5);

  const alocacao = await test("ALMOXARIFE - POST /alocacao-pecas para inativar", 200, () =>
    request("POST", "/alocacao-pecas", {
      token: tokenAlmoxarife,
      body: {
        quantidadeNecessaria: 1,
        prestacaoServicoId: prestacaoInativarAlocacao?.id,
        pecaId: pecaInativarAlocacao?.id,
      },
    }),
  );
  assertHasId("ALMOXARIFE - alocacao para inativar criada com id", alocacao.body);

  const alocacaoInativa = await test("ALMOXARIFE - PATCH /alocacao-pecas/{id}/inativar", 200, () =>
    request("PATCH", `/alocacao-pecas/${alocacao.body?.id}/inativar`, { token: tokenAlmoxarife }),
  );
  assertEquals("ALMOXARIFE - inativar alocacao altera status para INATIVO", alocacaoInativa.body?.status, "INATIVO");

  const servicoInativarPrestacao = await criarServicoSmoke("InativarPrestacao");
  const osInativarPrestacao = await criarOrdemSmoke("InativarPrestacao");
  const prestacaoInativar = await criarPrestacaoSmoke("InativarPrestacao", osInativarPrestacao?.id, servicoInativarPrestacao?.id);

  const prestacaoInativa = await test("MECANICO - PATCH /prestacao-servico/{id}/inativar", 200, () =>
    request("PATCH", `/prestacao-servico/${prestacaoInativar?.id}/inativar`, { token: tokenMecanico }),
  );
  assertEquals("MECANICO - inativar prestacao altera status para INATIVO", prestacaoInativa.body?.status, "INATIVO");
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
  await test("VALIDATION - POST /veiculos com placa invalida deve retornar 400 ou 422", [400, 422], () =>
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
  await runMecanicoSetupTests();
  await runAlmoxarifeTests();
  await runOrdemServicoExecutionFlowTests();
  await runRemainingEndpointTests();
  await runSecurityTests();
  await runValidationTests();

  printSummary();
  process.exitCode = failures.length > 0 ? 1 : 0;
}

main().catch((error) => {
  fail("execucao da suite", "concluir execucao", "erro fatal", error.message);
  printSummary();
  process.exitCode = 1;
});
