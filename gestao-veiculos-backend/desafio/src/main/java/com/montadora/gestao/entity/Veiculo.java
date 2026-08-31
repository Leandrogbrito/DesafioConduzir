package com.montadora.gestao.entity;

import com.montadora.gestao.enums.TipoCombustivel;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * ENTITY Veiculo = a tabela "veiculos" no banco.
 */
@Entity
@Table(name = "veiculos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----- Campos obrigatorios -----
    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Enumerated(EnumType.STRING) // Guarda o texto "FLEX" no banco, nao um numero
    @Column(nullable = false)
    private TipoCombustivel combustivel;

    @Column(nullable = false)
    private String cor;

    // ----- Campos opcionais (diferencial) -----
    private Integer ano;
    private String chassi;
    private BigDecimal valor;
    private String corExterna;

    /**
     * RELACIONAMENTO: muitos veiculos pertencem a UMA concessionaria.
     * "@JoinColumn" cria a coluna "concessionaria_id" na tabela veiculos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concessionaria_id")
    private Concessionaria concessionaria;
}
