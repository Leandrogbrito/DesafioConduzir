package com.montadora.gestao.dto;

/**
 * DTO de SAIDA da Concessionaria (o que a API DEVOLVE).
 * Inclui a quantidade de veiculos para dar uma visao rapida.
 */
public record ConcessionariaResponseDTO(
        Long id,
        String razaoSocial,
        String cnpj,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String estado,
        int quantidadeVeiculos
) {}
