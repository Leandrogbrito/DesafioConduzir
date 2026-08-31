package com.montadora.gestao.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Este e o nosso "carimbo" personalizado @CNPJ.
 * Quando colocamos @CNPJ em um campo, o Spring chama o CnpjValidator
 * para verificar se o numero e valido.
 */
@Documented
@Constraint(validatedBy = CnpjValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {
    String message() default "CNPJ invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
