package com.montadora.gestao.validation;

// Onde colocar: src/test/java/com/montadora/gestao/validation/CnpjValidatorTest.java
// (crie as pastas "test/java/com/montadora/gestao/validation" se nao existirem,
//  do lado de "main", nao dentro dele!)

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TESTE do validador de CNPJ (aquele que a gente corrigiu juntos!).
 * Aqui provamos, de forma automatica, que o calculo dos digitos
 * verificadores esta correto - tanto aceitando CNPJs validos
 * quanto rejeitando invalidos.
 */
class CnpjValidatorTest {

    private final CnpjValidator validator = new CnpjValidator();

    @ParameterizedTest(name = "CNPJ valido: {0}")
    @DisplayName("Deve aceitar CNPJs validos (com e sem mascara)")
    @ValueSource(strings = {
            "04.252.011/0001-10",
            "04252011000110",
            "11.222.333/0001-81"
    })
    void deveAceitarCnpjValido(String cnpj) {
        assertThat(validator.isValid(cnpj, null)).isTrue();
    }

    @ParameterizedTest(name = "CNPJ invalido: {0}")
    @DisplayName("Deve rejeitar CNPJs invalidos")
    @ValueSource(strings = {
            "11.111.111/1111-11", // todos iguais
            "12345678000100",     // digitos verificadores errados
            "123",                // curto demais
            "abcdefghijklmn"      // nao numerico
    })
    void deveRejeitarCnpjInvalido(String cnpj) {
        assertThat(validator.isValid(cnpj, null)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar valor nulo")
    void deveRejeitarNull() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
