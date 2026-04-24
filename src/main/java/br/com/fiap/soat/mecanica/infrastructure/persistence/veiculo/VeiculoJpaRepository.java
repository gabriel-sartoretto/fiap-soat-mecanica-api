package br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, UUID> {
}
