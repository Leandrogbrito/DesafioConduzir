package com.montadora.gestao.dto;

// Onde colocar: src/main/java/com/montadora/gestao/dto/VeiculoRecomendadoDTO.java

/**
 * Representa UM veiculo recomendado, com a "nota de compatibilidade" (0-100)
 * e o motivo em texto explicando por que ele foi sugerido.
 */
public record VeiculoRecomendadoDTO(
        VeiculoResponseDTO veiculo,
        int pontuacao,       // 0 a 100 (quanto maior, mais parecido com o pedido)
        String motivo        // motivo curto explicando o porque da recomendacao
) {}