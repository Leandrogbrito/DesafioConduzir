package com.montadora.gestao.mapper;

import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.entity.Concessionaria;
import com.montadora.gestao.entity.Veiculo;
import org.springframework.stereotype.Component;

/**
 * MAPPER = o "tradutor". Converte:
 *   DTO (marmita) -> Entity (comida do banco) e vice-versa.
 * Assim o Service nao fica cheio de codigo repetido.
 */
@Component
public class VeiculoMapper {

    // DTO de entrada -> Entity (para salvar no banco)
    public Veiculo toEntity(VeiculoRequestDTO dto, Concessionaria concessionaria) {
        return Veiculo.builder()
                .marca(dto.marca())
                .modelo(dto.modelo())
                .combustivel(dto.combustivel())
                .cor(dto.cor())
                .ano(dto.ano())
                .chassi(dto.chassi())
                .valor(dto.valor())
                .corExterna(dto.corExterna())
                .concessionaria(concessionaria)
                .build();
    }

    // Entity -> DTO de saida (para devolver ao usuario)
    public VeiculoResponseDTO toResponse(Veiculo v) {
        return new VeiculoResponseDTO(
                v.getId(),
                v.getMarca(),
                v.getModelo(),
                v.getCombustivel(),
                v.getCor(),
                v.getAno(),
                v.getChassi(),
                v.getValor(),
                v.getCorExterna(),
                v.getConcessionaria() != null ? v.getConcessionaria().getId() : null,
                v.getConcessionaria() != null ? v.getConcessionaria().getRazaoSocial() : null
        );
    }
}
