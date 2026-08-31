import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate, useParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { veiculoSchema, type VeiculoFormData } from '../schemas/veiculoSchema'
import { veiculosApi } from '../api/veiculos'
import { concessionariasApi } from '../api/concessionarias'
import { TIPOS_COMBUSTIVEL } from '../types'
import { useToast } from '../components/Toast'
import { Carregando } from '../components/Estados'

/**
 * PAGINA: Formulario de Veiculo (serve para CRIAR e EDITAR).
 * Usa React Hook Form + Zod para validar antes de enviar.
 */
export function VeiculoFormPage() {
  const { id } = useParams()
  const editando = Boolean(id)
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { mostrar } = useToast()

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<VeiculoFormData>({
    resolver: zodResolver(veiculoSchema),
  })

  // Busca as concessionarias para o <select>
  const { data: concessionarias } = useQuery({
    queryKey: ['concessionarias'],
    queryFn: concessionariasApi.listar,
  })

  // Se estiver editando, busca o veiculo e preenche o formulario
  const { data: veiculo, isLoading } = useQuery({
    queryKey: ['veiculo', id],
    queryFn: () => veiculosApi.buscarPorId(Number(id)),
    enabled: editando,
  })

  useEffect(() => {
    if (veiculo) reset(veiculo)
  }, [veiculo, reset])

  // Salvar (cria ou atualiza)
  const salvar = useMutation({
    mutationFn: (dados: VeiculoFormData) =>
      editando ? veiculosApi.atualizar(Number(id), dados) : veiculosApi.criar(dados),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['veiculos'] })
      mostrar(editando ? 'Veiculo atualizado!' : 'Veiculo cadastrado!')
      navigate('/veiculos')
    },
    onError: () => mostrar('Erro ao salvar veiculo.', 'erro'),
  })

  if (editando && isLoading) return <Carregando />

  return (
    <div className="card">
      <div className="page-header">
        <h2>{editando ? 'Editar Veiculo' : 'Novo Veiculo'}</h2>
      </div>

      <form onSubmit={handleSubmit((d) => salvar.mutate(d))}>
        <div className="form-grid">
          <div className="form-group">
            <label>Marca *</label>
            <input {...register('marca')} placeholder="Ex: Fiat" />
            {errors.marca && <span className="erro-msg">{errors.marca.message}</span>}
          </div>

          <div className="form-group">
            <label>Modelo *</label>
            <input {...register('modelo')} placeholder="Ex: Uno" />
            {errors.modelo && <span className="erro-msg">{errors.modelo.message}</span>}
          </div>

          <div className="form-group">
            <label>Combustivel *</label>
            <select {...register('combustivel')} defaultValue="">
              <option value="" disabled>
                Selecione...
              </option>
              {TIPOS_COMBUSTIVEL.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
            {errors.combustivel && (
              <span className="erro-msg">{errors.combustivel.message}</span>
            )}
          </div>

          <div className="form-group">
            <label>Cor *</label>
            <input {...register('cor')} placeholder="Ex: Vermelho" />
            {errors.cor && <span className="erro-msg">{errors.cor.message}</span>}
          </div>

          <div className="form-group">
            <label>Ano</label>
            <input type="number" {...register('ano')} placeholder="Ex: 2024" />
            {errors.ano && <span className="erro-msg">{errors.ano.message}</span>}
          </div>

          <div className="form-group">
            <label>Chassi</label>
            <input {...register('chassi')} placeholder="Opcional" />
          </div>

          <div className="form-group">
            <label>Valor (R$)</label>
            <input type="number" step="0.01" {...register('valor')} placeholder="Ex: 55000" />
          </div>

          <div className="form-group">
            <label>Cor Externa</label>
            <input {...register('corExterna')} placeholder="Opcional" />
          </div>

          <div className="form-group full">
            <label>Concessionaria</label>
            <select {...register('concessionariaId')} defaultValue="">
              <option value="">Nenhuma</option>
              {concessionarias?.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.razaoSocial}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-success" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : 'Salvar'}
          </button>
          <button type="button" className="btn btn-ghost" onClick={() => navigate('/veiculos')}>
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}
