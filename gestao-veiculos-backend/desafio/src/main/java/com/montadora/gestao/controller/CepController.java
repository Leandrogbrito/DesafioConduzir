package com.montadora.gestao.controller;

import com.montadora.gestao.dto.ViaCepResponse;
import com.montadora.gestao.service.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * DIFERENCIAL: endpoint que o FRONTEND chama ao digitar o CEP,
 * para preencher o endereco automaticamente na tela (antes de salvar).
 * Ex: GET /cep/58400000
 */
@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
@Tag(name = "CEP", description = "Consulta de endereco via ViaCEP")
public class CepController {

    private final ViaCepService viaCepService;

    @GetMapping("/{cep}")
    @Operation(summary = "Busca o endereco de um CEP no ViaCEP")
    public ViaCepResponse buscar(@PathVariable String cep) {
        return viaCepService.buscarPorCep(cep);
    }
}
