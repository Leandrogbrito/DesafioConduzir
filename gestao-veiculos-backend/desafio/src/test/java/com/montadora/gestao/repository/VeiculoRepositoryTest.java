package com.montadora.gestao.repository;

import com.montadora.gestao.entity.Concessionaria;
import com.montadora.gestao.entity.Veiculo;
import com.montadora.gestao.enums.TipoCombustivel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TESTE DE REPOSITORIO (@DataJpaTest) usando um banco H2 em memoria de verdade.
 * Provamos que a consulta findByConcessionariaId funciona de fato no banco.
 */
@DataJpaTest
class VeiculoRepositoryTest {

    @Autowired VeiculoRepository veiculoRepository;
    @Autowired ConcessionariaRepository concessionariaRepository;

    @Test
    @DisplayName("findByConcessionariaId deve retornar apenas os veiculos da concessionaria")
    void deveBuscarVeiculosPorConcessionaria() {
        // ARRANGE: cria uma concessionaria e dois veiculos vinculados
        Concessionaria conc = concessionariaRepository.save(
                Concessionaria.builder()
                        .razaoSocial("Auto JP")
                        .cnpj("11222333000181")
                        .build());

        veiculoRepository.save(Veiculo.builder()
                .marca("Fiat").modelo("Uno").cor("Vermelho")
                .combustivel(TipoCombustivel.FLEX).concessionaria(conc).build());
        veiculoRepository.save(Veiculo.builder()
                .marca("VW").modelo("Gol").cor("Prata")
                .combustivel(TipoCombustivel.GASOLINA).concessionaria(conc).build());
        // veiculo sem concessionaria (nao deve aparecer)
        veiculoRepository.save(Veiculo.builder()
                .marca("Ford").modelo("Ka").cor("Preto")
                .combustivel(TipoCombustivel.FLEX).build());

        // ACT
        List<Veiculo> encontrados = veiculoRepository.findByConcessionariaId(conc.getId());

        // ASSERT
        assertThat(encontrados).hasSize(2);
        assertThat(encontrados)
                .extracting(Veiculo::getMarca)
                .containsExactlyInAnyOrder("Fiat", "VW");
    }

    @Test
    @DisplayName("existsByCnpj deve identificar CNPJ ja cadastrado")
    void deveVerificarCnpjExistente() {
        concessionariaRepository.save(Concessionaria.builder()
                .razaoSocial("Loja A").cnpj("04252011000110").build());

        assertThat(concessionariaRepository.existsByCnpj("04252011000110")).isTrue();
        assertThat(concessionariaRepository.existsByCnpj("99999999999999")).isFalse();
    }
}
