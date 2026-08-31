package com.montadora.gestao.dto;

import com.montadora.gestao.enums.TipoCombustivel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO de ENTRADA (Request) = a "marmita" que o usuario ENVIA para criar/editar.
 * As anotacoes @NotBlank/@NotNull VALIDAM os campos obrigatorios automaticamente.
 */
public record VeiculoRequestDTO(

        @NotBlank(message = "Marca e obrigatoria")
        String marca,

        @NotBlank(message = "Modelo e obrigatorio")
        String modelo,

        @NotNull(message = "Combustivel e obrigatorio")
        TipoCombustivel combustivel,

        @NotBlank(message = "Cor e obrigatoria")
        String cor,

        // Opcionais
        Integer ano,
        String chassi,
        BigDecimal valor,
        String corExterna,
        Long concessionariaId
) {}
