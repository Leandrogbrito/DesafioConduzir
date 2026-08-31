package com.montadora.gestao.service;

// Onde colocar: src/test/java/com/montadora/gestao/service/VeiculoServiceTest.java

import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.entity.Veiculo;
import com.montadora.gestao.enums.TipoCombustivel;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import com.montadora.gestao.mapper.VeiculoMapper;
import com.montadora.gestao.repository.ConcessionariaRepository;
import com.montadora.gestao.repository.VeiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * TESTE UNITARIO do VeiculoService.
 * Usamos Mockito para "fingir" o banco (repository) e testar
 * SOMENTE a logica do Service, de forma rapida e isolada.
 */
@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock VeiculoRepository veiculoRepository;
    @Mock ConcessionariaRepository concessionariaRepository;
    @Mock VeiculoMapper mapper;

    @InjectMocks VeiculoService service;

    @Test
    @DisplayName("Deve criar veiculo com sucesso")
    void deveCriarVeiculoComSucesso() {
        var dto = new VeiculoRequestDTO("Fiat", "Uno", TipoCombustivel.FLEX,
                "Vermelho", 2024, null, null, null, null);

        var entity = Veiculo.builder().marca("Fiat").modelo("Uno").build();
        var salvo = Veiculo.builder().id(1L).marca("Fiat").modelo("Uno").build();
        var response = new VeiculoResponseDTO(1L, "Fiat", "Uno",
                TipoCombustivel.FLEX, "Vermelho", 2024, null, null, null, null, null);

        when(mapper.toEntity(dto, null)).thenReturn(entity);
        when(veiculoRepository.save(entity)).thenReturn(salvo);
        when(mapper.toResponse(salvo)).thenReturn(response);

        var resultado = service.criar(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.marca()).isEqualTo("Fiat");
        verify(veiculoRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Deve lancar excecao ao buscar veiculo inexistente")
    void deveLancarExcecaoQuandoVeiculoNaoExiste() {
        when(veiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve excluir veiculo existente")
    void deveExcluirVeiculo() {
        var veiculo = Veiculo.builder().id(5L).build();
        when(veiculoRepository.findById(5L)).thenReturn(Optional.of(veiculo));

        service.excluir(5L);

        verify(veiculoRepository, times(1)).delete(veiculo);
    }
}
