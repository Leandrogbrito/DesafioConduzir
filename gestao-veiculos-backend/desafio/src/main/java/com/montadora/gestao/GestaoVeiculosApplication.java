package com.montadora.gestao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ESTE E O "BOTAO DE LIGAR" DA APLICACAO.
 * Quando voce roda este arquivo, o Spring Boot acorda,
 * liga o servidor web e prepara tudo (banco, endpoints, etc.).
 */
@SpringBootApplication
public class GestaoVeiculosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoVeiculosApplication.class, args);
    }
}
