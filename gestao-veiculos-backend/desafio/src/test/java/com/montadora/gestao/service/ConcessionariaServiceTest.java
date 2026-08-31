package com.montadora.gestao.service;

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
 * TESTES do ConcessionariaService, focando em duas regras importantes:
 *   1) nao pode ter CNPJ duplicado;
 *   2) o endereco deve ser preenchido pelo ViaCEP quando o CEP e informado.
 */
@ExtendWith(MockitoExtension.class)
class ConcessionariaServiceTest {

    @Mock ConcessionariaRepository repository;
    @Mock ConcessionariaMapper mapper;
    @Mock ViaCepService viaCepService;

    @InjectMocks ConcessionariaService service;

    @Test
    @DisplayName("Deve lancar erro quando o CNPJ ja existe")
    void deveRejeitarCnpjDuplicado() {
        var dto = new ConcessionariaRequestDTO("Loja X", "11222333000181",
                null, null, null, null, null);
        when(repository.existsByCnpj("11222333000181")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CNPJ");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve preencher o endereco pelo ViaCEP quando o CEP e informado")
    void devePreencherEnderecoViaCep() {
        var dto = new ConcessionariaRequestDTO("Auto JP", "11222333000181",
                "58400-000", null, null, null, null);

        when(repository.existsByCnpj(anyString())).thenReturn(false);
        when(viaCepService.buscarPorCep("58400-000")).thenReturn(
                new ViaCepResponse("58400-000", "Rua Teste", "Centro",
                        "Campina Grande", "PB", null));
        when(repository.save(any(Concessionaria.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(
                new ConcessionariaResponseDTO(1L, "Auto JP", "11222333000181",
                        "58400-000", "Rua Teste", "Centro", "Campina Grande", "PB", 0));

        service.criar(dto);

        // Verifica que a entidade salva recebeu os dados do ViaCEP
        ArgumentCaptor<Concessionaria> captor = ArgumentCaptor.forClass(Concessionaria.class);
        verify(repository).save(captor.capture());
        Concessionaria salva = captor.getValue();

        assertThat(salva.getCidade()).isEqualTo("Campina Grande");
        assertThat(salva.getEstado()).isEqualTo("PB");
        assertThat(salva.getLogradouro()).isEqualTo("Rua Teste");
    }
}
