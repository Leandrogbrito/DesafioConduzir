import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate, useParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
  concessionariaSchema,
  type ConcessionariaFormData,
} from '../schemas/concessionariaSchema'
import { concessionariasApi } from '../api/concessionarias'
import { useToast } from '../components/Toast'
import { Carregando } from '../components/Estados'

/**
 * PAGINA: Formulario de Concessionaria (CRIAR e EDITAR).
 * DESTAQUE: ao digitar o CEP e sair do campo, busca o endereco no ViaCEP
 * e preenche logradouro, bairro, cidade e estado automaticamente.
 */
export function ConcessionariaFormPage() {
  const { id } = useParams()
  const editando = Boolean(id)
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { mostrar } = useToast()
  const [buscandoCep, setBuscandoCep] = useState(false)

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    getValues,
    formState: { errors, isSubmitting },
  } = useForm<ConcessionariaFormData>({
    resolver: zodResolver(concessionariaSchema),
  })

  // Se editando, carrega os dados
  const { data: concessionaria, isLoading } = useQuery({
    queryKey: ['concessionaria', id],
    queryFn: () => concessionariasApi.buscarPorId(Number(id)),
    enabled: editando,
  })

  useEffect(() => {
    if (concessionaria) reset(concessionaria)
  }, [concessionaria, reset])

  // A MAGICA DO VIACEP: dispara ao sair do campo CEP (onBlur)
  async function handleBuscarCep() {
    const cep = getValues('cep')?.replace(/\D/g, '')
    if (!cep || cep.length !== 8) return

    try {
      setBuscandoCep(true)
      const endereco = await concessionariasApi.buscarCep(cep)
      setValue('logradouro', endereco.logradouro)
      setValue('bairro', endereco.bairro)
      setValue('cidade', endereco.localidade)
      setValue('estado', endereco.uf)
      mostrar('Endereco preenchido pelo ViaCEP!')
    } catch {
      mostrar('CEP nao encontrado.', 'erro')
    } finally {
      setBuscandoCep(false)
    }
  }

  const salvar = useMutation({
    mutationFn: (dados: ConcessionariaFormData) =>
      editando
        ? concessionariasApi.atualizar(Number(id), dados)
        : concessionariasApi.criar(dados),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['concessionarias'] })
      mostrar(editando ? 'Concessionaria atualizada!' : 'Concessionaria cadastrada!')
      navigate('/concessionarias')
    },
    onError: () => mostrar('Erro ao salvar. Verifique o CNPJ.', 'erro'),
  })

  if (editando && isLoading) return <Carregando />

  return (
    <div className="card">
      <div className="page-header">
        <h2>{editando ? 'Editar Concessionaria' : 'Nova Concessionaria'}</h2>
      </div>

      <form onSubmit={handleSubmit((d) => salvar.mutate(d))}>
        <div className="form-grid">
          <div className="form-group full">
            <label>Razao Social *</label>
            <input {...register('razaoSocial')} placeholder="Ex: Auto Center JP" />
            {errors.razaoSocial && (
              <span className="erro-msg">{errors.razaoSocial.message}</span>
            )}
          </div>

          <div className="form-group">
            <label>CNPJ *</label>
            <input {...register('cnpj')} placeholder="00.000.000/0000-00" />
            {errors.cnpj && <span className="erro-msg">{errors.cnpj.message}</span>}
          </div>

          <div className="form-group">
            <label>CEP {buscandoCep && '(buscando...)'}</label>
            <input
              {...register('cep')}
              placeholder="58400-000"
              onBlur={handleBuscarCep}
            />
            <span style={{ fontSize: '0.72rem', color: '#64748b' }}>
              Digite o CEP para preencher o endereco automaticamente
            </span>
          </div>

          <div className="form-group full">
            <label>Logradouro</label>
            <input {...register('logradouro')} placeholder="Rua / Avenida" />
          </div>

          <div className="form-group">
            <label>Bairro</label>
            <input {...register('bairro')} />
          </div>

          <div className="form-group">
            <label>Cidade</label>
            <input {...register('cidade')} />
          </div>

          <div className="form-group">
            <label>Estado (UF)</label>
            <input {...register('estado')} maxLength={2} placeholder="PB" />
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-success" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : 'Salvar'}
          </button>
          <button
            type="button"
            className="btn btn-ghost"
            onClick={() => navigate('/concessionarias')}
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}
