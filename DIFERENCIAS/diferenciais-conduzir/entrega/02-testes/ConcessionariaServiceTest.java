package com.montadora.gestao.service;

// Onde colocar: src/test/java/com/montadora/gestao/service/ConcessionariaServiceTest.java

import com.montadora.gestao.dto.ConcessionariaRequestDTO;
import com.montadora.gestao.dto.ConcessionariaResponseDTO;
import com.montadora.gestao.dto.ViaCepResponse;
import com.montadora.gestao.entity.Concessionaria;
import com.montadora.gestao.mapper.ConcessionariaMapper;
import com.montadora.gestao.repository.ConcessionariaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * TESTES do ConcessionariaService: cobre a regra de CNPJ duplicado
 * e a integracao com o ViaCEP (o preenchimento automatico de endereco).
 */
@ExtendWith(MockitoExtension.class)
class ConcessionariaServiceTest {

    @Mock ConcessionariaRepository repository;
    @Mock ConcessionariaMapper mapper;
    @Mock ViaCepService viaCepService;

    @InjectMocks ConcessionariaService service;

    @Test
    @DisplayName("Deve rejeitar CNPJ duplicado")
    void deveRejeitarCnpjDuplicado() {
        var dto = new ConcessionariaRequestDTO("Loja X", "04252011000110",
                null, null, null, null, null);
        when(repository.existsByCnpj("04252011000110")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CNPJ");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve preencher endereco automaticamente via ViaCEP")
    void devePreencherEnderecoViaCep() {
        var dto = new ConcessionariaRequestDTO("Auto JP", "04252011000110",
                "58067-201", null, null, null, null);

        when(repository.existsByCnpj(anyString())).thenReturn(false);
        when(viaCepService.buscarPorCep("58067-201")).thenReturn(
                new ViaCepResponse("58067-201", "Rua Teste", "Centro",
                        "Joao Pessoa", "PB", null));
        when(repository.save(any(Concessionaria.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(
                new ConcessionariaResponseDTO(1L, "Auto JP", "04252011000110",
                        "58067-201", "Rua Teste", "Centro", "Joao Pessoa", "PB", 0));

        service.criar(dto);

        ArgumentCaptor<Concessionaria> captor = ArgumentCaptor.forClass(Concessionaria.class);
        verify(repository).save(captor.capture());

        assertThat(captor.getValue().getCidade()).isEqualTo("Joao Pessoa");
        assertThat(captor.getValue().getEstado()).isEqualTo("PB");
    }
}
