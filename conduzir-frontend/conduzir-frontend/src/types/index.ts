// Aqui ficam os "moldes" (tipos) que descrevem como sao os dados.
// O TypeScript usa isso para nos avisar se erramos algum campo.

// Os tipos de combustivel permitidos (igual ao enum do backend)
export const TIPOS_COMBUSTIVEL = [
  'GASOLINA',
  'ETANOL',
  'FLEX',
  'DIESEL',
  'ELETRICO',
  'HIBRIDO',
] as const

export type TipoCombustivel = (typeof TIPOS_COMBUSTIVEL)[number]

// Como vem um Veiculo da API (resposta)
export interface Veiculo {
  id: number
  marca: string
  modelo: string
  combustivel: TipoCombustivel
  cor: string
  ano?: number
  chassi?: string
  valor?: number
  corExterna?: string
  concessionariaId?: number
  concessionariaNome?: string
}

// Como vem uma Concessionaria da API (resposta)
export interface Concessionaria {
  id: number
  razaoSocial: string
  cnpj: string
  cep?: string
  logradouro?: string
  bairro?: string
  cidade?: string
  estado?: string
  quantidadeVeiculos: number
}
