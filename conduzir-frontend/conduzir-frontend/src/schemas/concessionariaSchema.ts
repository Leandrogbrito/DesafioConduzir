import { z } from 'zod'

// Funcao que valida o CNPJ calculando os digitos verificadores (mesma regra do backend).
function validarCnpj(cnpj: string): boolean {
  const numeros = cnpj.replace(/\D/g, '')
  if (numeros.length !== 14) return false
  if (/^(\d)\1{13}$/.test(numeros)) return false // rejeita todos iguais

  const calcularDigito = (base: string): number => {
    const pesos = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
    const offset = pesos.length - base.length
    let soma = 0
    for (let i = 0; i < base.length; i++) {
      soma += Number(base[i]) * pesos[offset + i]
    }
    const resto = soma % 11
    return resto < 2 ? 0 : 11 - resto
  }

  const d1 = calcularDigito(numeros.substring(0, 12))
  const d2 = calcularDigito(numeros.substring(0, 12) + d1)
  return numeros === numeros.substring(0, 12) + d1 + d2
}

// SCHEMA de validacao da Concessionaria
export const concessionariaSchema = z.object({
  razaoSocial: z.string().min(1, 'Razao social e obrigatoria'),
  cnpj: z
    .string()
    .min(1, 'CNPJ e obrigatorio')
    .refine(validarCnpj, 'CNPJ invalido'),
  cep: z.string().optional(),
  logradouro: z.string().optional(),
  bairro: z.string().optional(),
  cidade: z.string().optional(),
  estado: z.string().optional(),
})

export type ConcessionariaFormData = z.infer<typeof concessionariaSchema>
