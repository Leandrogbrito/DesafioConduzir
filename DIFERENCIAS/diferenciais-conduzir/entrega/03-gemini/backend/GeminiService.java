package com.montadora.gestao.service;

// Onde colocar: src/main/java/com/montadora/gestao/service/GeminiService.java

import com.montadora.gestao.dto.RecomendacaoRequestDTO;
import com.montadora.gestao.dto.VeiculoRecomendadoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Este service "liga" para a IA do GOOGLE GEMINI e pede para ela
 * escrever, em portugues natural, uma explicacao sobre por que
 * recomendamos determinados veiculos ao cliente.
 *
 * IMPORTANTE: se a chave (gemini.api.key) nao estiver configurada,
 * o metodo isConfigured() retorna false, e o RecomendacaoService usa
 * um resumo local no lugar (a aplicacao NUNCA quebra por falta de IA).
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    // Modelo rapido e gratuito do Gemini (bom custo-beneficio)
    private static final String MODELO = "gemini-1.5-flash";

    private final RestClient restClient;
    private final String apiKey;

    public GeminiService(RestClient.Builder builder,
                          @Value("${gemini.api.key:}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    /** Diz se a chave do Gemini foi configurada (application.properties ou variavel de ambiente). */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Monta um "prompt" (pedido em texto) descrevendo o que o cliente quer
     * e os veiculos ja pre-selecionados pelo nosso algoritmo de pontuacao,
     * e pede para o Gemini escrever uma explicacao curta e amigavel.
     */
    public String gerarExplicacao(RecomendacaoRequestDTO pedido, List<VeiculoRecomendadoDTO> top) {
        String prompt = montarPrompt(pedido, top);

        // Corpo da requisicao no formato que a API do Gemini espera
        Map<String, Object> corpo = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        Map<?, ?> resposta = restClient.post()
                .uri("/models/{modelo}:generateContent?key={key}", MODELO, apiKey)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(corpo)
                .retrieve()
                .body(Map.class);

        return extrairTexto(resposta);
    }

    private String montarPrompt(RecomendacaoRequestDTO pedido, List<VeiculoRecomendadoDTO> top) {
        StringBuilder sb = new StringBuilder();
        sb.append("Voce e um consultor de vendas de veiculos, simpatico e direto. ");
        sb.append("Um cliente busca um carro com estas preferencias: ");
        sb.append("modelo=").append(pedido.modelo() != null ? pedido.modelo() : "qualquer").append(", ");
        sb.append("combustivel=").append(pedido.combustivel() != null ? pedido.combustivel() : "qualquer").append(", ");
        sb.append("cor=").append(pedido.cor() != null ? pedido.cor() : "qualquer").append(", ");
        sb.append("ano=").append(pedido.ano() != null ? pedido.ano() : "qualquer").append(". ");
        sb.append("Estes sao os veiculos disponiveis, ja ordenados por compatibilidade (0 a 100): ");

        for (VeiculoRecomendadoDTO r : top) {
            sb.append(String.format("[%s %s, %s, cor %s, ano %s, compatibilidade %d%%] ",
                    r.veiculo().marca(), r.veiculo().modelo(), r.veiculo().combustivel(),
                    r.veiculo().cor(), r.veiculo().ano(), r.pontuacao()));
        }

        sb.append("Escreva uma explicacao curta (maximo 4 frases), em portugues, ");
        sb.append("recomendando o melhor veiculo da lista e justificando o motivo. ");
        sb.append("Seja natural, como um vendedor de verdade, sem usar markdown.");

        return sb.toString();
    }

    /** Extrai o texto de dentro da resposta JSON complexa que o Gemini devolve. */
    @SuppressWarnings("unchecked")
    private String extrairTexto(Map<?, ?> resposta) {
        try {
            var candidates = (List<Map<String, Object>>) resposta.get("candidates");
            var content = (Map<String, Object>) candidates.get(0).get("content");
            var parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            log.error("Nao foi possivel interpretar a resposta do Gemini", e);
            throw new IllegalStateException("Resposta inesperada do Gemini");
        }
    }
}
