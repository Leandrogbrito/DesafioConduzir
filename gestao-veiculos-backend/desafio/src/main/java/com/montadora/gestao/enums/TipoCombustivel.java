package com.montadora.gestao.enums;

/**
 * ENUM = uma LISTA FIXA de opcoes permitidas.
 * O combustivel so pode ser UM destes valores. Nada mais.
 * Isso evita que alguem cadastre "gasolinaa" errado.
 */
public enum TipoCombustivel {
    GASOLINA,
    ETANOL,
    FLEX,
    DIESEL,
    ELETRICO,
    HIBRIDO
}
