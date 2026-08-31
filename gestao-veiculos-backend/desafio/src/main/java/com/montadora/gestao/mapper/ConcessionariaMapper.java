package com.montadora.gestao.mapper;

import com.montadora.gestao.dto.ConcessionariaResponseDTO;
import com.montadora.gestao.entity.Concessionaria;
import org.springframework.stereotype.Component;

/**
 * Tradutor da Concessionaria: Entity -> DTO de saida.
 */
@Component
public class ConcessionariaMapper {

    public ConcessionariaResponseDTO toResponse(Concessionaria c) {
        return new ConcessionariaResponseDTO(
                c.getId(),
                c.getRazaoSocial(),
                c.getCnpj(),
                c.getCep(),
                c.getLogradouro(),
                c.getBairro(),
                c.getCidade(),
                c.getEstado(),
                c.getVeiculos() != null ? c.getVeiculos().size() : 0
        );
    }
}
