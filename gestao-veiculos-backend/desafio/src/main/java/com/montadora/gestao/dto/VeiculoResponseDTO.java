package com.montadora.gestao.dto;

import com.montadora.gestao.enums.TipoCombustivel;
import java.math.BigDecimal;

/**
 * DTO de SAIDA (Response) = a "marmita" que a API DEVOLVE para o usuario.
 * Aqui devolvemos so o que interessa, sem expor a Entity inteira do banco.
 */
public record VeiculoResponseDTO(
        Long id,
        String marca,
        String modelo,
        TipoCombustivel combustivel,
        String cor,
        Integer ano,
        String chassi,
        BigDecimal valor,
        String corExterna,
        Long concessionariaId,
        String concessionariaNome
) {}
