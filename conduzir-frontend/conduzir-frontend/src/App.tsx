import { Navigate, Route, Routes } from 'react-router-dom'
import { Layout } from './components/Layout'
import { VeiculosListPage } from './pages/VeiculosListPage'
import { VeiculoFormPage } from './pages/VeiculoFormPage'
import { ConcessionariasListPage } from './pages/ConcessionariasListPage'
import { ConcessionariaFormPage } from './pages/ConcessionariaFormPage'
import { RecomendacaoPage } from './pages/RecomendacaoPage'

/**
 * APP = o "mapa" da aplicacao (React Router).
 * Cada URL mostra uma pagina diferente, todas dentro do Layout (moldura).
 */
export function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        {/* Redireciona a raiz para /veiculos */}
        <Route index element={<Navigate to="/veiculos" replace />} />

        {/* Veiculos */}
        <Route path="veiculos" element={<VeiculosListPage />} />
        <Route path="veiculos/novo" element={<VeiculoFormPage />} />
        <Route path="veiculos/:id/editar" element={<VeiculoFormPage />} />

        {/* Concessionarias */}
        <Route path="concessionarias" element={<ConcessionariasListPage />} />
        <Route path="concessionarias/nova" element={<ConcessionariaFormPage />} />
        <Route path="concessionarias/:id/editar" element={<ConcessionariaFormPage />} />
        <Route path="recomendacao" element={<RecomendacaoPage />} />
      </Route>
    </Routes>
  )
}
