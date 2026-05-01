package br.com.fiap.soat.mecanica.adapters.in.web.cliente;

import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteAlterarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.cliente.mapper.ClienteResponseMapper;
import br.com.fiap.soat.mecanica.application.cliente.usecase.*;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final AlterarClienteUseCase alterarClienteUseCase;
    private final BuscarClientePorCpfUseCase buscarClientePorCpfUseCase;
    private final BuscarClientePorCnpjUseCase buscarClientePorCnpjUseCase;
    private final BuscarClientePorUsuarioIdUseCase buscarClientePorUsuarioIdUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    @Operation(summary = "Cadastrar um cliente")
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody ClienteIncluirRequest request) {
        Cliente cliente = cadastrarClienteUseCase.executar(request.nome(), request.cpf(), request.cnpj(),
                request.email(), request.telefone());
        return ResponseEntity.ok(ClienteResponseMapper.toResponse(cliente));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    @Operation(summary = "Alterar um cliente")
    public ResponseEntity<ClienteResponse> alterar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteAlterarRequest request) {
        Cliente cliente = alterarClienteUseCase.executar(id, request.nome(), request.telefone());
        return ResponseEntity.ok(ClienteResponseMapper.toResponse(cliente));
    }

    @GetMapping("/por-cpf/{cpf}")
    @PreAuthorize("hasRole('ATENDENTE')")
    @Operation(summary = "Buscar cliente por CPF")
    public ResponseEntity<ClienteResponse> buscarPorCpf(@PathVariable String cpf) {

        Cliente cliente = buscarClientePorCpfUseCase.executar(cpf);

        return ResponseEntity.ok(ClienteResponseMapper.toResponse(cliente));
    }

    @GetMapping("/por-cnpj/{cnpj}")
    @PreAuthorize("hasRole('ATENDENTE')")
    @Operation(summary = "Buscar cliente por CNPJ")
    public ResponseEntity<ClienteResponse> buscarPorCnpj(@PathVariable String cnpj) {

        Cliente cliente = buscarClientePorCnpjUseCase.executar(cnpj);

        return ResponseEntity.ok(ClienteResponseMapper.toResponse(cliente));
    }

    @GetMapping("/por-usuario/{usuarioId}")
    @PreAuthorize("hasRole('ATENDENTE')")
    @Operation(summary = "Cadastrar um cliente")
    public ResponseEntity<ClienteResponse> buscarTodosPorUsuarioId(UUID usuarioId) {
        Cliente cliente = buscarClientePorUsuarioIdUseCase.executar(usuarioId);
        return ResponseEntity.ok(ClienteResponseMapper.toResponse(cliente));
    }
}
