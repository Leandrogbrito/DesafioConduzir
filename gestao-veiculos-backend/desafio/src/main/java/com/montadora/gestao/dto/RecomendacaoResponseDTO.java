package com.montadora.gestao.dto;

// Onde colocar: src/main/java/com/montadora/gestao/dto/RecomendacaoResponseDTO.java

import java.util.List;

/**
 * Resposta completa do Recomendador:
 * - a lista de veiculos sugeridos (ja ordenados do melhor para o pior)
 * - um resumo em texto (gerado pela IA do Gemini, se configurada;
 *   senao, um resumo simples gerado pelo proprio Java)
 */
public record RecomendacaoResponseDTO(
        List<VeiculoRecomendadoDTO> recomendacoes,
        String resumoIA,           // texto explicativo (IA ou fallback local)
        boolean geradoPorIA        // true = veio do Gemini, false = gerado localmente
) {}