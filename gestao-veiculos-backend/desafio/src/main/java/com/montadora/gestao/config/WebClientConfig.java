package com.montadora.gestao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Aqui criamos o "telefone" (RestClient) que usaremos para LIGAR
 * para a API externa do ViaCEP. Deixamos ele pronto para ser reaproveitado.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public RestClient viaCepRestClient() {
        return RestClient.builder()
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }
}
