package com.montadora.gestao.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TESTE do validador de CNPJ.
 * Aqui provamos que a regra dos digitos verificadores funciona:
 * aceita CNPJ valido e rejeita CNPJ invalido.
 */
class CnpjValidatorTest {

    private final CnpjValidator validator = new CnpjValidator();

    @ParameterizedTest
    @DisplayName("Deve aceitar CNPJs validos (com e sem mascara)")
    @ValueSource(strings = {
            "11.222.333/0001-81",
            "11222333000181",
            "04.252.011/0001-10"
    })
    void deveAceitarCnpjValido(String cnpj) {
        assertThat(validator.isValid(cnpj, null)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("Deve rejeitar CNPJs invalidos")
    @ValueSource(strings = {
            "11.222.333/0001-99", // digito verificador errado
            "12345678000100",     // digitos errados
            "00000000000000",     // todos iguais
            "123",                // curto demais
            "abcdefghijklmn"      // nao numerico
    })
    void deveRejeitarCnpjInvalido(String cnpj) {
        assertThat(validator.isValid(cnpj, null)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar null")
    void deveRejeitarNull() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
