import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Configuracao do Vite (a "ferramenta que liga o app rapidinho")
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // PROXY: tudo que comeca com /api vai para o backend em localhost:8080.
    // Assim evitamos problemas de CORS durante o desenvolvimento.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
