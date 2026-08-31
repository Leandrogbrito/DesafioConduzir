import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { veiculosApi } from '../api/veiculos'
import { Carregando, ErroAoCarregar, ListaVazia } from '../components/Estados'
import { useToast } from '../components/Toast'

/**
 * PAGINA: Lista de Veiculos.
 * Usa React Query para BUSCAR (useQuery) e EXCLUIR (useMutation).
 */
export function VeiculosListPage() {
  const queryClient = useQueryClient()
  const { mostrar } = useToast()

  // BUSCA a lista de veiculos (com loading e erro automaticos)
  const { data: veiculos, isLoading, isError, error } = useQuery({
    queryKey: ['veiculos'],
    queryFn: veiculosApi.listar,
  })

  // EXCLUIR um veiculo
  const excluir = useMutation({
    mutationFn: veiculosApi.excluir,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['veiculos'] }) // atualiza a lista
      mostrar('Veiculo excluido com sucesso!')
    },
    onError: () => mostrar('Erro ao excluir veiculo.', 'erro'),
  })

  function handleExcluir(id: number, nome: string) {
    if (confirm(`Deseja realmente excluir o veiculo "${nome}"?`)) {
      excluir.mutate(id)
    }
  }

  if (isLoading) return <Carregando />
  if (isError) return <ErroAoCarregar mensagem={(error as Error).message} />

  return (
    <div className="card">
      <div className="page-header">
        <h2>Veiculos</h2>
        <Link to="/veiculos/novo" className="btn btn-primary">
          + Novo Veiculo
        </Link>
      </div>

      {!veiculos || veiculos.length === 0 ? (
        <ListaVazia texto="Nenhum veiculo cadastrado ainda. Clique em '+ Novo Veiculo'." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Marca</th>
              <th>Modelo</th>
              <th>Combustivel</th>
              <th>Cor</th>
              <th>Concessionaria</th>
              <th style={{ textAlign: 'right' }}>Acoes</th>
            </tr>
          </thead>
          <tbody>
            {veiculos.map((v) => (
              <tr key={v.id}>
                <td>{v.marca}</td>
                <td>{v.modelo}</td>
                <td>
                  <span className="badge">{v.combustivel}</span>
                </td>
                <td>{v.cor}</td>
                <td>{v.concessionariaNome ?? '-'}</td>
                <td style={{ textAlign: 'right' }}>
                  <Link to={`/veiculos/${v.id}/editar`} className="btn btn-sm btn-ghost">
                    Editar
                  </Link>{' '}
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleExcluir(v.id, `${v.marca} ${v.modelo}`)}
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
