package br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class VeiculoEntity {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
    private UUID id;

    private String placa;
}
