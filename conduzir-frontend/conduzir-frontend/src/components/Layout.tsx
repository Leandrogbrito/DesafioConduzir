import { NavLink, Outlet } from 'react-router-dom'

/**
 * LAYOUT = a "moldura" que aparece em todas as telas:
 * o cabecalho com a marca CONDUZIR + o slogan, e o menu de navegacao.
 * O <Outlet /> e o "buraco" onde cada pagina e desenhada.
 */
export function Layout() {
  return (
    <>
      <header className="header">
        <div className="header-content">
          <img src="/logo.svg" alt="Conduzir" className="header-logo" />
          <div className="header-brand">
            <h1>Conduzir</h1>
            <p>Guiando o seu negocio pelo melhor caminho.</p>
          </div>
          <nav>
            <NavLink to="/veiculos" className={({ isActive }) => (isActive ? 'ativo' : '')}>
              Veiculos
            </NavLink>
            <NavLink to="/concessionarias" className={({ isActive }) => (isActive ? 'ativo' : '')}>
              Concessionarias
            </NavLink>
            <NavLink to="/recomendacao" className={({ isActive }) => (isActive ? 'ativo' : '')}>
            🤖 Recomendação IA
            </NavLink>
          </nav>
        </div>
      </header>

      <main className="container">
        <Outlet />
      </main>
    </>
  )
}
