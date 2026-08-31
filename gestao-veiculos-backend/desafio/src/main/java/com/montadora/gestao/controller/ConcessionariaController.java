package com.montadora.gestao.controller;

import com.montadora.gestao.dto.ConcessionariaRequestDTO;
import com.montadora.gestao.dto.ConcessionariaResponseDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.service.ConcessionariaService;
import com.montadora.gestao.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER (GARCOM) da Concessionaria.
 * Endpoints em /dealer, exatamente como pede o desafio.
 */
@RestController
@RequestMapping("/dealer")
@RequiredArgsConstructor
@Tag(name = "Concessionarias", description = "Operacoes de cadastro de concessionarias")
public class ConcessionariaController {

    private final ConcessionariaService service;
    private final VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Lista todas as concessionarias")
    public List<ConcessionariaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma concessionaria pelo id")
    public ConcessionariaResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cria concessionaria (busca endereco no ViaCEP se enviar o CEP)")
    public ResponseEntity<ConcessionariaResponseDTO> criar(
            @Valid @RequestBody ConcessionariaRequestDTO dto) {
        ConcessionariaResponseDTO criado = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma concessionaria")
    public ConcessionariaResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConcessionariaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma concessionaria")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Requisito de ASSOCIACAO: listar veiculos de uma concessionaria
    @GetMapping("/{id}/vehicles")
    @Operation(summary = "Lista os veiculos de uma concessionaria")
    public List<VeiculoResponseDTO> listarVeiculos(@PathVariable Long id) {
        return veiculoService.listarPorConcessionaria(id);
    }
}
