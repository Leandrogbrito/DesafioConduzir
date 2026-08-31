package com.montadora.gestao.controller;

import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
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
 * CONTROLLER = o "GARCOM". Recebe as requisicoes HTTP (o pedido de fora),
 * chama o Service e devolve a resposta. NAO tem regra de negocio aqui!
 *
 * @Tag e @Operation aparecem bonitos no Swagger (diferencial).
 */
@RestController
@RequestMapping("/vehicles") // Todos os endpoints comecam com /vehicles
@RequiredArgsConstructor
@Tag(name = "Veiculos", description = "Operacoes de cadastro de veiculos")
public class VeiculoController {

    private final VeiculoService service;

    @GetMapping
    @Operation(summary = "Lista todos os veiculos")
    public List<VeiculoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um veiculo pelo id")
    public VeiculoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cria um novo veiculo")
    public ResponseEntity<VeiculoResponseDTO> criar(@Valid @RequestBody VeiculoRequestDTO dto) {
        VeiculoResponseDTO criado = service.criar(dto);
        // 201 Created = "criei algo novo com sucesso"
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veiculo existente")
    public VeiculoResponseDTO atualizar(@PathVariable Long id,
                                        @Valid @RequestBody VeiculoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um veiculo")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        // 204 No Content = "deu certo, mas nao tenho nada para devolver"
        return ResponseEntity.noContent().build();
    }

    // ASSOCIACAO: vincular/trocar a concessionaria de um veiculo
    // Ex: PATCH /vehicles/5/dealer/2
    @PatchMapping("/{id}/dealer/{dealerId}")
    @Operation(summary = "Associa ou troca a concessionaria de um veiculo")
    public VeiculoResponseDTO associarConcessionaria(@PathVariable Long id,
                                                     @PathVariable Long dealerId) {
        return service.associarConcessionaria(id, dealerId);
    }
}
