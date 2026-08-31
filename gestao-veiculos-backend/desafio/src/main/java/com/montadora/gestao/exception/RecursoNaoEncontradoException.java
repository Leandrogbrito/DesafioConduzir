package com.montadora.gestao.exception;

/**
 * Nossa excecao personalizada: usada quando procuramos algo (ex: veiculo id 99)
 * e nao encontramos. Depois o @RestControllerAdvice transforma isso num 404 bonito.
 */
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
