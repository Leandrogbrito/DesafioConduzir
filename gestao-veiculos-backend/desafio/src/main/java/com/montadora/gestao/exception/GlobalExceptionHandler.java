package com.montadora.gestao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DIFERENCIAL: TRATAMENTO DE ERROS CENTRALIZADO.
 * Em vez de a aplicacao "quebrar feio", aqui capturamos os erros
 * e devolvemos uma resposta JSON organizada. O gerente ama isso!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Quando nao encontramos um recurso -> 404 Not Found
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", 404);
        corpo.put("erro", "Recurso nao encontrado");
        corpo.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    // Quando uma regra de negocio e violada (ex: CNPJ duplicado, CEP invalido) -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleRegraNegocio(IllegalArgumentException ex) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", 400);
        corpo.put("erro", "Requisicao invalida");
        corpo.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    // Quando a validacao falha (@NotBlank etc.) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, Object> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                erros.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", 400);
        corpo.put("erro", "Validacao falhou");
        corpo.put("campos", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }
}
