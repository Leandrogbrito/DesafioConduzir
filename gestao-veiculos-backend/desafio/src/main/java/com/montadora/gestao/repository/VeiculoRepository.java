package com.montadora.gestao.repository;

import com.montadora.gestao.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * REPOSITORY = o "despenseiro". So sabe pegar e guardar dados.
 * Ao estender JpaRepository, voce JA GANHA de graca:
 *   save(), findAll(), findById(), deleteById()... sem escrever nada!
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    // O Spring cria o SQL sozinho so pelo nome do metodo (magica!):
    // SELECT * FROM veiculos WHERE concessionaria_id = ?
    List<Veiculo> findByConcessionariaId(Long concessionariaId);
}
