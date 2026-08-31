package com.montadora.gestao.service;

import com.montadora.gestao.dto.ViaCepResponse;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * DIFERENCIAL: INTEGRACAO COM API EXTERNA (ViaCEP).
 * Este service "liga" para o ViaCEP passando um CEP e recebe o endereco.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ViaCepService {

    private final RestClient viaCepRestClient;

    public ViaCepResponse buscarPorCep(String cep) {
        // 1) Limpa o CEP: tira tracos e espacos -> "58400-000" vira "58400000"
        String cepLimpo = cep.replaceAll("\\D", "");

        // 2) Validacao simples: CEP precisa ter 8 digitos
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 digitos numericos");
        }

        log.info("Consultando ViaCEP para o CEP {}", cepLimpo);

        // 3) LIGA para o ViaCEP: GET https://viacep.com.br/ws/58400000/json
        ViaCepResponse resposta = viaCepRestClient.get()
                .uri("/{cep}/json", cepLimpo)
                .retrieve()
                .body(ViaCepResponse.class);

        // 4) Se o ViaCEP devolver "erro": true, o CEP nao existe
        if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
            throw new RecursoNaoEncontradoException("CEP nao encontrado: " + cep);
        }

        return resposta;
    }
}
