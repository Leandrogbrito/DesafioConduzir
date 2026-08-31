import { z } from 'zod'
import { TIPOS_COMBUSTIVEL } from '../types'

// SCHEMA = as REGRAS de validacao do formulario de Veiculo.
// O Zod confere tudo ANTES de enviar para o backend (dupla protecao).
export const veiculoSchema = z.object({
  marca: z.string().min(1, 'Marca e obrigatoria'),
  modelo: z.string().min(1, 'Modelo e obrigatorio'),
  combustivel: z.enum(TIPOS_COMBUSTIVEL, {
    errorMap: () => ({ message: 'Selecione um combustivel' }),
  }),
  cor: z.string().min(1, 'Cor e obrigatoria'),

  // Campos opcionais (coerce converte texto do input em numero)
  ano: z.coerce
    .number()
    .int()
    .min(1900, 'Ano invalido')
    .max(2100, 'Ano invalido')
    .optional()
    .or(z.literal(undefined)),
  chassi: z.string().optional(),
  valor: z.coerce.number().min(0, 'Valor invalido').optional().or(z.literal(undefined)),
  corExterna: z.string().optional(),
  concessionariaId: z.coerce.number().optional().or(z.literal(undefined)),
})

// O tipo TypeScript e gerado AUTOMATICAMENTE a partir do schema. Que magica!
export type VeiculoFormData = z.infer<typeof veiculoSchema>
