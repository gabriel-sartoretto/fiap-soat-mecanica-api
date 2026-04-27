package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, UUID> {
}
