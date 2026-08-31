import { api } from './client'
import type { Concessionaria } from '../types'
import type { ConcessionariaFormData } from '../schemas/concessionariaSchema'

// Chamadas para os endpoints de Concessionarias (/dealer) e CEP (/cep).

export interface EnderecoViaCep {
  cep: string
  logradouro: string
  bairro: string
  localidade: string // cidade
  uf: string // estado
}

export const concessionariasApi = {
  // GET /dealer
  listar: async (): Promise<Concessionaria[]> => {
    const { data } = await api.get<Concessionaria[]>('/dealer')
    return data
  },

  // GET /dealer/{id}
  buscarPorId: async (id: number): Promise<Concessionaria> => {
    const { data } = await api.get<Concessionaria>(`/dealer/${id}`)
    return data
  },

  // POST /dealer
  criar: async (dados: ConcessionariaFormData): Promise<Concessionaria> => {
    const { data } = await api.post<Concessionaria>('/dealer', dados)
    return data
  },

  // PUT /dealer/{id}
  atualizar: async (
    id: number,
    dados: ConcessionariaFormData
  ): Promise<Concessionaria> => {
    const { data } = await api.put<Concessionaria>(`/dealer/${id}`, dados)
    return data
  },

  // DELETE /dealer/{id}
  excluir: async (id: number): Promise<void> => {
    await api.delete(`/dealer/${id}`)
  },

  // GET /cep/{cep} - busca endereco no ViaCEP (diferencial)
  buscarCep: async (cep: string): Promise<EnderecoViaCep> => {
    const cepLimpo = cep.replace(/\D/g, '')
    const { data } = await api.get<EnderecoViaCep>(`/cep/${cepLimpo}`)
    return data
  },
}
