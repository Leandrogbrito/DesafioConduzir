package com.montadora.gestao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTITY = a "etiqueta" que liga esta classe Java a uma TABELA do banco.
 * Cada objeto Concessionaria = uma linha na tabela "concessionarias".
 */
@Entity
@Table(name = "concessionarias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Concessionaria {

    @Id // Chave primaria (o "numero de identidade" da linha)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera o ID sozinho
    private Long id;

    @Column(nullable = false)
    private String razaoSocial;

    @Column(nullable = false, unique = true) // CNPJ nao pode repetir
    private String cnpj;

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;

    /**
     * RELACIONAMENTO: uma concessionaria tem VARIOS veiculos.
     * "mappedBy" diz que o dono do relacionamento esta na classe Veiculo.
     */
    @OneToMany(mappedBy = "concessionaria", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Veiculo> veiculos = new ArrayList<>();
}
