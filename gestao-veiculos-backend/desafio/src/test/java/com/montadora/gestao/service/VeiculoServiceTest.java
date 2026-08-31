package com.montadora.gestao.service;

import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.entity.Veiculo;
import com.montadora.gestao.enums.TipoCombustivel;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import com.montadora.gestao.mapper.VeiculoMapper;
import com.montadora.gestao.repository.ConcessionariaRepository;
import com.montadora.gestao.repository.VeiculoRepository;
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
 * DIFERENCIAL: TESTE UNITARIO.
 * Usamos Mockito para "fingir" (mock) o banco, e testamos SO a regra do Service.
 * Assim testamos rapido, sem precisar de banco de verdade.
 */
@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock VeiculoRepository veiculoRepository;
    @Mock ConcessionariaRepository concessionariaRepository;
    @Mock VeiculoMapper mapper;

    @InjectMocks VeiculoService service;

    @Test
    void deveCriarVeiculoComSucesso() {
        // ARRANGE (preparar o cenario)
        var dto = new VeiculoRequestDTO("Fiat", "Uno",
                TipoCombustivel.FLEX, "Vermelho",
                null, null, null, null, null);

        var entity = Veiculo.builder().marca("Fiat").modelo("Uno").build();
        var salvo = Veiculo.builder().id(1L).marca("Fiat").modelo("Uno").build();
        var response = new VeiculoResponseDTO(1L, "Fiat", "Uno",
                TipoCombustivel.FLEX, "Vermelho", null, null, null, null, null, null);

        when(mapper.toEntity(dto, null)).thenReturn(entity);
        when(veiculoRepository.save(entity)).thenReturn(salvo);
        when(mapper.toResponse(salvo)).thenReturn(response);

        // ACT (executar a acao)
        var resultado = service.criar(dto);

        // ASSERT (verificar o resultado)
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.marca()).isEqualTo("Fiat");
        verify(veiculoRepository, times(1)).save(entity);
    }

    @Test
    void deveLancarExcecaoQuandoVeiculoNaoExiste() {
        when(veiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }
}
