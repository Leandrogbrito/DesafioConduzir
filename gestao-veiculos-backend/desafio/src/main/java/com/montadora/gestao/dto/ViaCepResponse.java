package com.montadora.gestao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Esta e a "marmita" que o ViaCEP nos DEVOLVE quando pedimos um CEP.
 * O @JsonIgnoreProperties ignora campos extras que nao usamos (ex: ddd, ibge).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(
        String cep,
        String logradouro,  // ex: "Rua Fulano"
        String bairro,      // ex: "Centro"
        String localidade,  // ATENCAO: no ViaCEP a CIDADE se chama "localidade"
        String uf,          // ATENCAO: o ESTADO se chama "uf"
        Boolean erro        // ViaCEP devolve "erro": true quando o CEP nao existe
) {}
