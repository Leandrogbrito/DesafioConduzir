// Onde colocar: src/pages/RecomendacaoPage.tsx (arquivo NOVO)

import { useState } from 'react'
import { useMutation } from '@tanstack/react-query'
import { recomendacaoApi, type RecomendacaoResponse } from '../api/recomendacao'
import { TIPOS_COMBUSTIVEL, type TipoCombustivel } from '../types'

/**
 * PAGINA: Recomendacao Inteligente de Veiculos.
 * O usuario preenche o que deseja (tudo opcional) e recebe uma
 * lista de veiculos ordenados por compatibilidade, com uma
 * explicacao gerada por IA (Gemini) ou, na ausencia dela,
 * um resumo gerado localmente.
 */
export function RecomendacaoPage() {
  const [modelo, setModelo] = useState('')
  const [combustivel, setCombustivel] = useState<TipoCombustivel | ''>('')
  const [cor, setCor] = useState('')
  const [ano, setAno] = useState('')
  const [resultado, setResultado] = useState<RecomendacaoResponse | null>(null)

  const buscar = useMutation({
    mutationFn: recomendacaoApi.recomendar,
    onSuccess: (data) => setResultado(data),
  })

  function handleBuscar(e: React.FormEvent) {
    e.preventDefault()
    buscar.mutate({
      modelo: modelo || undefined,
      combustivel: (combustivel as TipoCombustivel) || undefined,
      cor: cor || undefined,
      ano: ano ? Number(ano) : undefined,
      quantidade: 5,
    })
  }

  return (
    <div className="card">
      <div className="page-header">
        <h2>🤖 Recomendação Inteligente de Veículos</h2>
      </div>

      <p style={{ color: '#64748b', marginBottom: 20 }}>
        Diga o que você procura e nossa IA sugere os veículos mais compatíveis do estoque.
      </p>

      <form onSubmit={handleBuscar}>
        <div className="form-grid">
          <div className="form-group">
            <label>Modelo desejado</label>
            <input
              value={modelo}
              onChange={(e) => setModelo(e.target.value)}
              placeholder="Ex: Corolla"
            />
          </div>

          <div className="form-group">
            <label>Combustível</label>
            <select value={combustivel} onChange={(e) => setCombustivel(e.target.value as TipoCombustivel)}>
              <option value="">Qualquer</option>
              {TIPOS_COMBUSTIVEL.map((t) => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Cor</label>
            <input
              value={cor}
              onChange={(e) => setCor(e.target.value)}
              placeholder="Ex: Prata"
            />
          </div>

          <div className="form-group">
            <label>Ano</label>
            <input
              type="number"
              value={ano}
              onChange={(e) => setAno(e.target.value)}
              placeholder="Ex: 2024"
            />
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={buscar.isPending}>
            {buscar.isPending ? 'Consultando IA...' : '🔍 Gerar Recomendação'}
          </button>
        </div>
      </form>

      {resultado && (
        <div style={{ marginTop: 28 }}>
          <div
            style={{
              background: '#eef6ff',
              borderLeft: '4px solid #1e5a9e',
              padding: '14px 18px',
              borderRadius: 8,
              marginBottom: 20,
            }}
          >
            <strong>{resultado.geradoPorIA ? '🤖 Explicação da IA (Gemini):' : '💡 Resumo automático:'}</strong>
            <p style={{ marginTop: 6 }}>{resultado.resumoIA}</p>
          </div>

          <h3 style={{ marginBottom: 12, color: '#0f3a6b' }}>Veículos sugeridos</h3>

          {resultado.recomendacoes.length === 0 ? (
            <p>Nenhum veículo cadastrado ainda para recomendar.</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Compatibilidade</th>
                  <th>Veículo</th>
                  <th>Combustível</th>
                  <th>Cor</th>
                  <th>Ano</th>
                  <th>Motivo</th>
                </tr>
              </thead>
              <tbody>
                {resultado.recomendacoes.map((r, i) => (
                  <tr key={i}>
                    <td>
                      <span
                        className="badge"
                        style={{
                          background: r.pontuacao >= 70 ? '#e6f7f3' : '#fff7ed',
                          color: r.pontuacao >= 70 ? '#2ec4a6' : '#f59e0b',
                        }}
                      >
                        {r.pontuacao}%
                      </span>
                    </td>
                    <td>{r.veiculo.marca} {r.veiculo.modelo}</td>
                    <td>{r.veiculo.combustivel}</td>
                    <td>{r.veiculo.cor}</td>
                    <td>{r.veiculo.ano ?? '-'}</td>
                    <td style={{ fontSize: '0.82rem', color: '#64748b' }}>{r.motivo}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  )
}
