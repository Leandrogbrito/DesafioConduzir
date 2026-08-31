package com.montadora.gestao.service;

import com.montadora.gestao.dto.ConcessionariaRequestDTO;
import com.montadora.gestao.dto.ConcessionariaResponseDTO;
import com.montadora.gestao.dto.ViaCepResponse;
import com.montadora.gestao.entity.Concessionaria;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import com.montadora.gestao.mapper.ConcessionariaMapper;
import com.montadora.gestao.repository.ConcessionariaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE (CHEF) da Concessionaria. Aqui ficam as regras,
 * inclusive a "magica" de buscar o endereco pelo CEP no ViaCEP.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConcessionariaService {

    private final ConcessionariaRepository repository;
    private final ConcessionariaMapper mapper;
    private final ViaCepService viaCepService; // o nosso "telefone" para o ViaCEP

    // CREATE
    @Transactional
    public ConcessionariaResponseDTO criar(ConcessionariaRequestDTO dto) {
        log.info("Criando concessionaria: {}", dto.razaoSocial());

        // Regra de negocio: nao pode ter dois CNPJs iguais
        if (repository.existsByCnpj(dto.cnpj())) {
            throw new IllegalArgumentException("Ja existe concessionaria com este CNPJ");
        }

        Concessionaria entidade = Concessionaria.builder()
                .razaoSocial(dto.razaoSocial())
                .cnpj(dto.cnpj())
                .build();

        // Preenche o endereco (pelo ViaCEP ou manualmente)
        preencherEndereco(entidade, dto);

        return mapper.toResponse(repository.save(entidade));
    }

    // READ - todos
    @Transactional(readOnly = true)
    public List<ConcessionariaResponseDTO> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    // READ - por id
    @Transactional(readOnly = true)
    public ConcessionariaResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    // UPDATE
    @Transactional
    public ConcessionariaResponseDTO atualizar(Long id, ConcessionariaRequestDTO dto) {
        Concessionaria entidade = buscarEntidade(id);
        entidade.setRazaoSocial(dto.razaoSocial());
        entidade.setCnpj(dto.cnpj());
        preencherEndereco(entidade, dto);
        return mapper.toResponse(repository.save(entidade));
    }

    // DELETE
    @Transactional
    public void excluir(Long id) {
        Concessionaria entidade = buscarEntidade(id);
        repository.delete(entidade);
    }

    // ---------- Metodos auxiliares ----------

    private Concessionaria buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Concessionaria nao encontrada com id " + id));
    }

    /**
     * A MAGICA DO VIACEP:
     * - Se o usuario informou um CEP -> busca o endereco no ViaCEP e preenche sozinho.
     * - Se nao informou CEP -> usa o endereco que ele digitou na mao.
     */
    private void preencherEndereco(Concessionaria entidade, ConcessionariaRequestDTO dto) {
        if (dto.cep() != null && !dto.cep().isBlank()) {
            ViaCepResponse via = viaCepService.buscarPorCep(dto.cep());
            entidade.setCep(via.cep());
            entidade.setLogradouro(via.logradouro());
            entidade.setBairro(via.bairro());
            entidade.setCidade(via.localidade()); // localidade = cidade
            entidade.setEstado(via.uf());         // uf = estado
        } else {
            entidade.setCep(dto.cep());
            entidade.setLogradouro(dto.logradouro());
            entidade.setBairro(dto.bairro());
            entidade.setCidade(dto.cidade());
            entidade.setEstado(dto.estado());
        }
    }
}
