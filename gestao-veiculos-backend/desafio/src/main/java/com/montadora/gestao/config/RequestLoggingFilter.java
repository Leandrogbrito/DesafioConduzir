package com.montadora.gestao.config;

// Onde colocar: src/main/java/com/montadora/gestao/config/RequestLoggingFilter.java

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * FILTRO DE LOGS ESTRUTURADOS (observabilidade).
 *
 * Para CADA requisicao que chega na API, este filtro:
 *   1) Gera um ID UNICO (requestId) para aquela requisicao
 *   2) Registra no log: metodo HTTP, URL, tempo de resposta e status
 *   3) Coloca o requestId no MDC (Mapped Diagnostic Context), que faz
 *      TODOS os logs daquela requisicao (mesmo os que voce ja escreveu
 *      com log.info nos Services) ganharem automaticamente esse mesmo ID.
 *
 * Isso permite, em producao, filtrar TODOS os logs de uma unica requisicao
 * do usuario - essencial para depurar problemas (ex: "por que o CEP 12345
 * deu erro?" -> busca o requestId e ve a jornada completa).
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID_KEY, requestId);

        long inicio = System.currentTimeMillis();

        try {
            log.info("Requisicao recebida: {} {}", request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
            long duracaoMs = System.currentTimeMillis() - inicio;
            log.info("Requisicao finalizada: {} {} -> status={} duracaoMs={}",
                    request.getMethod(), request.getRequestURI(),
                    response.getStatus(), duracaoMs);

            // Limpa o MDC para nao "vazar" o requestId para a proxima requisicao
            MDC.remove(REQUEST_ID_KEY);
        }
    }
}
