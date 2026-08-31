package com.montadora.gestao.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Aqui esta a "receita" que confere se o CNPJ e verdadeiro.
 * O CNPJ tem 14 numeros e os 2 ultimos sao "digitos verificadores":
 * eles sao calculados a partir dos outros. Se a conta bater, o CNPJ e valido.
 */
public class CnpjValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        // 1) Deixa so os numeros: "11.222.333/0001-81" -> "11222333000181"
        String cnpj = value.replaceAll("\\D", "");

        // 2) Precisa ter 14 digitos
        if (cnpj.length() != 14) return false;

        // 3) Rejeita numeros todos iguais (ex: 00000000000000)
        if (cnpj.chars().distinct().count() == 1) return false;

        // 4) Calcula e compara os 2 digitos verificadores
        try {
            int primeiroDigito = calcularDigito(cnpj.substring(0, 12));
            int segundoDigito  = calcularDigito(cnpj.substring(0, 12) + primeiroDigito);

            return cnpj.equals(cnpj.substring(0, 12) + primeiroDigito + segundoDigito);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Calcula um digito verificador usando a regra oficial (modulo 11)
    private int calcularDigito(String base) {
    	int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        // Alinha os pesos ao tamanho da base (12 o13 digitos)
        int offset = pesos.length - base.length();

        for (int i = 0; i < base.length(); i++) {
            int numero = Character.getNumericValue(base.charAt(i));
            soma += numero * pesos[offset + i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}
