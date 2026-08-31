import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { concessionariasApi } from '../api/concessionarias'
import { Carregando, ErroAoCarregar, ListaVazia } from '../components/Estados'
import { useToast } from '../components/Toast'

/**
 * PAGINA: Lista de Concessionarias.
 */
export function ConcessionariasListPage() {
  const queryClient = useQueryClient()
  const { mostrar } = useToast()

  const { data: lista, isLoading, isError, error } = useQuery({
    queryKey: ['concessionarias'],
    queryFn: concessionariasApi.listar,
  })

  const excluir = useMutation({
    mutationFn: concessionariasApi.excluir,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['concessionarias'] })
      mostrar('Concessionaria excluida com sucesso!')
    },
    onError: () => mostrar('Erro ao excluir concessionaria.', 'erro'),
  })

  function handleExcluir(id: number, nome: string) {
    if (confirm(`Deseja realmente excluir "${nome}"?`)) {
      excluir.mutate(id)
    }
  }

  if (isLoading) return <Carregando />
  if (isError) return <ErroAoCarregar mensagem={(error as Error).message} />

  return (
    <div className="card">
      <div className="page-header">
        <h2>Concessionarias</h2>
        <Link to="/concessionarias/nova" className="btn btn-primary">
          + Nova Concessionaria
        </Link>
      </div>

      {!lista || lista.length === 0 ? (
        <ListaVazia texto="Nenhuma concessionaria cadastrada ainda." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Razao Social</th>
              <th>CNPJ</th>
              <th>Cidade/UF</th>
              <th>Veiculos</th>
              <th style={{ textAlign: 'right' }}>Acoes</th>
            </tr>
          </thead>
          <tbody>
            {lista.map((c) => (
              <tr key={c.id}>
                <td>{c.razaoSocial}</td>
                <td>{c.cnpj}</td>
                <td>
                  {c.cidade ? `${c.cidade}/${c.estado}` : '-'}
                </td>
                <td>
                  <span className="badge">{c.quantidadeVeiculos}</span>
                </td>
                <td style={{ textAlign: 'right' }}>
                  <Link to={`/concessionarias/${c.id}/editar`} className="btn btn-sm btn-ghost">
                    Editar
                  </Link>{' '}
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleExcluir(c.id, c.razaoSocial)}
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
