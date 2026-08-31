package com.montadora.gestao.controller;

// Onde colocar: src/main/java/com/montadora/gestao/controller/RecomendacaoController.java

import com.montadora.gestao.dto.RecomendacaoRequestDTO;
import com.montadora.gestao.dto.RecomendacaoResponseDTO;
import com.montadora.gestao.service.RecomendacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint da funcionalidade de IA: recomendacao inteligente de veiculos.
 * POST /recommendations
 */
@RestController
@RequestMapping("/recommendations")
@Tag(name = "Recomendacao IA", description = "Sugere veiculos com base em preferencias do cliente")
public class RecomendacaoController {

    private final RecomendacaoService service;

    public RecomendacaoController(RecomendacaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Recomenda veiculos com base em modelo, combustivel, cor e ano desejados")
    public RecomendacaoResponseDTO recomendar(@RequestBody RecomendacaoRequestDTO pedido) {
        return service.recomendar(pedido);
    }
}