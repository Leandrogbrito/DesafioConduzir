// Componentes reutilizaveis para os 3 estados de UX pedidos no desafio:
// carregando (loading), erro e lista vazia.

export function Carregando() {
  return (
    <div className="estado">
      <div className="spinner" />
      <p>Carregando...</p>
    </div>
  )
}

export function ErroAoCarregar({ mensagem }: { mensagem?: string }) {
  return (
    <div className="estado">
      <p>Ops! Nao foi possivel carregar os dados.</p>
      {mensagem && <p style={{ fontSize: '0.85rem' }}>{mensagem}</p>}
    </div>
  )
}

export function ListaVazia({ texto }: { texto: string }) {
  return (
    <div className="estado">
      <p>{texto}</p>
    </div>
  )
}
