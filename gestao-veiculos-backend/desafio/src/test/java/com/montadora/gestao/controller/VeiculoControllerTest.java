package com.montadora.gestao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.enums.TipoCombustivel;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import com.montadora.gestao.service.VeiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTE DE INTEGRACAO da camada web (Controller) usando MockMvc.
 * Aqui simulamos requisicoes HTTP de verdade e checamos os status/JSON.
 * O Service e "mockado" para isolarmos o Controller.
 */
@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean VeiculoService service;

    @Test
    @DisplayName("GET /vehicles deve retornar 200 e a lista")
    void deveListarVeiculos() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new VeiculoResponseDTO(1L, "Fiat", "Uno", TipoCombustivel.FLEX,
                        "Vermelho", null, null, null, null, null, null)));

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Fiat"))
                .andExpect(jsonPath("$[0].modelo").value("Uno"));
    }

    @Test
    @DisplayName("POST /vehicles valido deve retornar 201 Created")
    void deveCriarVeiculo() throws Exception {
        var request = new VeiculoRequestDTO("Fiat", "Uno", TipoCombustivel.FLEX,
                "Vermelho", null, null, null, null, null);
        var response = new VeiculoResponseDTO(1L, "Fiat", "Uno", TipoCombustivel.FLEX,
                "Vermelho", null, null, null, null, null, null);

        when(service.criar(any())).thenReturn(response);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /vehicles invalido (sem marca) deve retornar 400")
    void deveRejeitarVeiculoInvalido() throws Exception {
        var invalido = new VeiculoRequestDTO("", "Uno", TipoCombustivel.FLEX,
                "Vermelho", null, null, null, null, null);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /vehicles/{id} inexistente deve retornar 404")
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Veiculo nao encontrado com id 99"));

        mockMvc.perform(get("/vehicles/99"))
                .andExpect(status().isNotFound());
    }
}
