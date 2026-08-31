package com.montadora.gestao.repository;

import com.montadora.gestao.entity.Concessionaria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * REPOSITORY da Concessionaria. Mesma ideia: ganha o CRUD de graca.
 */
public interface ConcessionariaRepository extends JpaRepository<Concessionaria, Long> {

    // Serve para checar se o CNPJ ja existe antes de cadastrar:
    boolean existsByCnpj(String cnpj);
}
