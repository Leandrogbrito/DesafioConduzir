package com.montadora.gestao.service;

// Onde colocar: src/main/java/com/montadora/gestao/service/RecomendacaoService.java

import com.montadora.gestao.dto.*;
import com.montadora.gestao.entity.Veiculo;
import com.montadora.gestao.mapper.VeiculoMapper;
import com.montadora.gestao.repository.VeiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * O "VENDEDOR EXPERIENTE" da Conduzir.
 *
 * Este service calcula, para cada veiculo do banco, uma PONTUACAO (0-100)
 * de quanto ele "combina" com o que o cliente pediu (modelo, combustivel,
 * cor, ano). Depois, ordena do melhor para o pior e devolve o Top N.
 *
 * Opcionalmente, se o Gemini estiver configurado, pedimos para a IA
 * escrever uma explicacao em texto natural sobre o resultado.
 */
@Service
public class RecomendacaoService {

    private static final Logger log = LoggerFactory.getLogger(RecomendacaoService.class);

    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;
    private final GeminiService geminiService;

    public RecomendacaoService(VeiculoRepository veiculoRepository,
                                VeiculoMapper veiculoMapper,
                                GeminiService geminiService) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
        this.geminiService = geminiService;
    }

    public RecomendacaoResponseDTO recomendar(RecomendacaoRequestDTO pedido) {
        int quantidade = (pedido.quantidade() != null && pedido.quantidade() > 0)
                ? pedido.quantidade() : 5;

        List<Veiculo> todos = veiculoRepository.findAll();

        List<VeiculoRecomendadoDTO> recomendados = todos.stream()
                .map(v -> calcularPontuacao(v, pedido))
                .sorted(Comparator.comparingInt(VeiculoRecomendadoDTO::pontuacao).reversed())
                .limit(quantidade)
                .toList();

        log.info("Recomendacao gerada: {} veiculo(s) avaliados, top {} retornados",
                todos.size(), recomendados.size());

        String resumo;
        boolean geradoPorIA;
        if (geminiService.isConfigured()) {
            try {
                resumo = geminiService.gerarExplicacao(pedido, recomendados);
                geradoPorIA = true;
            } catch (Exception e) {
                log.warn("Falha ao consultar Gemini, usando resumo local. Motivo: {}", e.getMessage());
                resumo = gerarResumoLocal(recomendados);
                geradoPorIA = false;
            }
        } else {
            resumo = gerarResumoLocal(recomendados);
            geradoPorIA = false;
        }

        return new RecomendacaoResponseDTO(recomendados, resumo, geradoPorIA);
    }

    /**
     * O ALGORITMO DE PONTUACAO.
     * Cada criterio que "bate" com o pedido soma pontos:
     *   - Combustivel igual:        +30 pontos
     *   - Cor igual:                +25 pontos
     *   - Modelo parecido/contido:  +30 pontos
     *   - Ano igual:                +15 pontos
     *   - Ano proximo (+-1 ano):    +7  pontos
     */
    private VeiculoRecomendadoDTO calcularPontuacao(Veiculo v, RecomendacaoRequestDTO pedido) {
        int pontos = 0;
        StringBuilder motivo = new StringBuilder();

        if (pedido.combustivel() != null) {
            if (pedido.combustivel().equals(v.getCombustivel())) {
                pontos += 30;
                motivo.append("combustivel ").append(v.getCombustivel()).append("; ");
            }
        }

        if (pedido.cor() != null && !pedido.cor().isBlank()) {
            if (v.getCor() != null && v.getCor().equalsIgnoreCase(pedido.cor().trim())) {
                pontos += 25;
                motivo.append("cor ").append(v.getCor()).append("; ");
            }
        }

        if (pedido.modelo() != null && !pedido.modelo().isBlank()) {
            String modeloPedido = pedido.modelo().trim().toLowerCase();
            String modeloVeiculo = (v.getModelo() != null ? v.getModelo() : "").toLowerCase();
            String marcaVeiculo = (v.getMarca() != null ? v.getMarca() : "").toLowerCase();

            if (modeloVeiculo.contains(modeloPedido) || modeloPedido.contains(modeloVeiculo)) {
                pontos += 30;
                motivo.append("modelo ").append(v.getModelo()).append("; ");
            } else if (marcaVeiculo.contains(modeloPedido)) {
                pontos += 15;
                motivo.append("marca ").append(v.getMarca()).append("; ");
            }
        }

        if (pedido.ano() != null && v.getAno() != null) {
            int diferenca = Math.abs(pedido.ano() - v.getAno());
            if (diferenca == 0) {
                pontos += 15;
                motivo.append("ano ").append(v.getAno()).append("; ");
            } else if (diferenca == 1) {
                pontos += 7;
                motivo.append("ano proximo (").append(v.getAno()).append("); ");
            }
        }

        boolean nenhumCriterio = pedido.modelo() == null && pedido.combustivel() == null
                && pedido.cor() == null && pedido.ano() == null;
        if (nenhumCriterio) {
            pontos = 50;
            motivo.append("sem filtros informados - exibindo veiculos disponiveis; ");
        }

        String motivoFinal = motivo.isEmpty()
                ? "Nenhum criterio em comum com o pedido."
                : "Compatibilidade em: " + motivo.substring(0, motivo.length() - 2) + ".";

        return new VeiculoRecomendadoDTO(veiculoMapper.toResponse(v), pontos, motivoFinal);
    }

    /**
     * Resumo gerado LOCALMENTE (sem IA), usado como "fallback" caso
     * o Gemini nao esteja configurado ou esteja indisponivel.
     */
    private String gerarResumoLocal(List<VeiculoRecomendadoDTO> recomendados) {
        if (recomendados.isEmpty()) {
            return "Nenhum veiculo cadastrado no momento para gerar recomendacoes.";
        }
        VeiculoRecomendadoDTO melhor = recomendados.get(0);
        return String.format(
                "Com base nos criterios informados, o veiculo mais compatível é o %s %s, "
                        + "com %d%% de compatibilidade. %s",
                melhor.veiculo().marca(), melhor.veiculo().modelo(),
                melhor.pontuacao(), melhor.motivo());
    }
}