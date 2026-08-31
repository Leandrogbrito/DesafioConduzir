package com.montadora.gestao.service;

import com.montadora.gestao.dto.VeiculoRequestDTO;
import com.montadora.gestao.dto.VeiculoResponseDTO;
import com.montadora.gestao.entity.Concessionaria;
import com.montadora.gestao.entity.Veiculo;
import com.montadora.gestao.exception.RecursoNaoEncontradoException;
import com.montadora.gestao.mapper.VeiculoMapper;
import com.montadora.gestao.repository.ConcessionariaRepository;
import com.montadora.gestao.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE = o "CHEF". Aqui ficam as REGRAS DE NEGOCIO.
 * O Controller nunca fala direto com o Repository; ele passa pelo Service.
 */
@Service
@RequiredArgsConstructor // Lombok injeta os "ingredientes" (repositories) no construtor
@Slf4j                   // Habilita os logs (log.info, log.error...)
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ConcessionariaRepository concessionariaRepository;
    private final VeiculoMapper mapper;

    // CREATE - criar veiculo
    @Transactional
    public VeiculoResponseDTO criar(VeiculoRequestDTO dto) {
        log.info("Criando veiculo: {} {}", dto.marca(), dto.modelo());

        Concessionaria concessionaria = buscarConcessionariaSeInformada(dto.concessionariaId());
        Veiculo veiculo = mapper.toEntity(dto, concessionaria);
        Veiculo salvo = veiculoRepository.save(veiculo);

        log.info("Veiculo criado com id {}", salvo.getId());
        return mapper.toResponse(salvo);
    }

    // READ - listar todos
    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> listar() {
        return veiculoRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    // READ - buscar por id
    @Transactional(readOnly = true)
    public VeiculoResponseDTO buscarPorId(Long id) {
        Veiculo veiculo = buscarEntidade(id);
        return mapper.toResponse(veiculo);
    }

    // UPDATE - atualizar veiculo
    @Transactional
    public VeiculoResponseDTO atualizar(Long id, VeiculoRequestDTO dto) {
        log.info("Atualizando veiculo id {}", id);
        Veiculo veiculo = buscarEntidade(id);

        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setCombustivel(dto.combustivel());
        veiculo.setCor(dto.cor());
        veiculo.setAno(dto.ano());
        veiculo.setChassi(dto.chassi());
        veiculo.setValor(dto.valor());
        veiculo.setCorExterna(dto.corExterna());
        veiculo.setConcessionaria(buscarConcessionariaSeInformada(dto.concessionariaId()));

        return mapper.toResponse(veiculoRepository.save(veiculo));
    }

    // DELETE - excluir veiculo
    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo veiculo id {}", id);
        Veiculo veiculo = buscarEntidade(id);
        veiculoRepository.delete(veiculo);
    }

    // LISTAR veiculos por concessionaria (requisito de associacao)
    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> listarPorConcessionaria(Long concessionariaId) {
        return veiculoRepository.findByConcessionariaId(concessionariaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ASSOCIAR / TROCAR a concessionaria de um veiculo (requisito de associacao)
    @Transactional
    public VeiculoResponseDTO associarConcessionaria(Long veiculoId, Long concessionariaId) {
        log.info("Associando veiculo {} a concessionaria {}", veiculoId, concessionariaId);
        Veiculo veiculo = buscarEntidade(veiculoId);
        veiculo.setConcessionaria(buscarConcessionariaSeInformada(concessionariaId));
        return mapper.toResponse(veiculoRepository.save(veiculo));
    }

    // ---------- Metodos auxiliares (privados) ----------

    private Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Veiculo nao encontrado com id " + id));
    }

    private Concessionaria buscarConcessionariaSeInformada(Long concessionariaId) {
        if (concessionariaId == null) return null;
        return concessionariaRepository.findById(concessionariaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Concessionaria nao encontrada com id " + concessionariaId));
    }
}
