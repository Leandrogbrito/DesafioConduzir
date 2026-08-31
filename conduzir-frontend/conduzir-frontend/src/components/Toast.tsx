import { createContext, useCallback, useContext, useState, type ReactNode } from 'react'

// TOAST = aquela mensagenzinha que aparece no canto ("Salvo com sucesso!").
type TipoToast = 'sucesso' | 'erro'
interface ToastContextType {
  mostrar: (mensagem: string, tipo?: TipoToast) => void
}

const ToastContext = createContext<ToastContextType | undefined>(undefined)

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toast, setToast] = useState<{ mensagem: string; tipo: TipoToast } | null>(null)

  const mostrar = useCallback((mensagem: string, tipo: TipoToast = 'sucesso') => {
    setToast({ mensagem, tipo })
    setTimeout(() => setToast(null), 3000) // some depois de 3 segundos
  }, [])

  return (
    <ToastContext.Provider value={{ mostrar }}>
      {children}
      {toast && <div className={`toast ${toast.tipo}`}>{toast.mensagem}</div>}
    </ToastContext.Provider>
  )
}

// Atalho para usar o toast em qualquer pagina: const { mostrar } = useToast()
export function useToast() {
  const ctx = useContext(ToastContext)
  if (!ctx) throw new Error('useToast precisa estar dentro do ToastProvider')
  return ctx
}
