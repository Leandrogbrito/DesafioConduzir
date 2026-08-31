package com.montadora.gestao.dto;

// Onde colocar: src/main/java/com/montadora/gestao/dto/RecomendacaoRequestDTO.java

import com.montadora.gestao.enums.TipoCombustivel;

/**
 * DTO de ENTRADA para o Recomendador de veiculos.
 * TODOS os campos sao OPCIONAIS - o usuario preenche so o que quiser.
 * Ex: so o combustivel, ou so a cor, ou tudo junto.
 */
public record RecomendacaoRequestDTO(
        String modelo,               // ex: "Corolla" (busca parecido, nao precisa ser exato)
        TipoCombustivel combustivel,
        String cor,
        Integer ano,
        Integer quantidade           // quantos resultados quer (padrao: 5)
) {}
