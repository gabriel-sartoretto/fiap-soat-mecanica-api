package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByCpf(String cpf);

    Optional<ClienteEntity> findByCnpj(String cnpj);

    Optional<ClienteEntity> findByUsuarioId(UUID usuarioId);
}
