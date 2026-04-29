package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PecaJpaRepository extends JpaRepository<PecaEntity, UUID> {

    Optional<PecaEntity> findByNome(String nome);
}
