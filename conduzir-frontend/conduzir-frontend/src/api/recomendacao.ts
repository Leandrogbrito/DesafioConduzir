import { api } from './client'
import type { TipoCombustivel, Veiculo } from '../types'

export interface RecomendacaoRequest {
  modelo?: string
  combustivel?: TipoCombustivel
  cor?: string
  ano?: number
  quantidade?: number
}

export interface VeiculoRecomendado {
  veiculo: Veiculo
  pontuacao: number
  motivo: string
}

export interface RecomendacaoResponse {
  recomendacoes: VeiculoRecomendado[]
  resumoIA: string
  geradoPorIA: boolean
}

// Chama o endpoint POST /recommendations do backend
export const recomendacaoApi = {
  recomendar: async (pedido: RecomendacaoRequest): Promise<RecomendacaoResponse> => {
    const { data } = await api.post<RecomendacaoResponse>('/recommendations', pedido)
    return data
  },
}