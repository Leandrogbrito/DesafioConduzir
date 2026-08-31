import { api } from './client'
import type { Veiculo } from '../types'
import type { VeiculoFormData } from '../schemas/veiculoSchema'

// Todas as "chamadas telefonicas" para os endpoints de Veiculos do backend.

export const veiculosApi = {
  // GET /vehicles
  listar: async (): Promise<Veiculo[]> => {
    const { data } = await api.get<Veiculo[]>('/vehicles')
    return data
  },

  // GET /vehicles/{id}
  buscarPorId: async (id: number): Promise<Veiculo> => {
    const { data } = await api.get<Veiculo>(`/vehicles/${id}`)
    return data
  },

  // POST /vehicles
  criar: async (dados: VeiculoFormData): Promise<Veiculo> => {
    const { data } = await api.post<Veiculo>('/vehicles', dados)
    return data
  },

  // PUT /vehicles/{id}
  atualizar: async (id: number, dados: VeiculoFormData): Promise<Veiculo> => {
    const { data } = await api.put<Veiculo>(`/vehicles/${id}`, dados)
    return data
  },

  // DELETE /vehicles/{id}
  excluir: async (id: number): Promise<void> => {
    await api.delete(`/vehicles/${id}`)
  },
}
