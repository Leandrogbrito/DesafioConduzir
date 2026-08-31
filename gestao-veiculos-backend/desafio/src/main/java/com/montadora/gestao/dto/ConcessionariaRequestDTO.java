package com.montadora.gestao.dto;

import com.montadora.gestao.validation.CNPJ;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de ENTRADA da Concessionaria (o que o usuario ENVIA).
 * Repare no @CNPJ (nosso carimbo) validando o campo automaticamente.
 *
 * Se o usuario mandar o CEP, o sistema busca o endereco no ViaCEP sozinho.
 * Mas ele tambem pode preencher o endereco na mao (por isso os campos ficam aqui).
 */
public record ConcessionariaRequestDTO(

        @NotBlank(message = "Razao social e obrigatoria")
        String razaoSocial,

        @NotBlank(message = "CNPJ e obrigatorio")
        @CNPJ
        String cnpj,

        String cep,        // se informado, dispara a busca no ViaCEP
        String logradouro,
        String bairro,
        String cidade,
        String estado
) {}
