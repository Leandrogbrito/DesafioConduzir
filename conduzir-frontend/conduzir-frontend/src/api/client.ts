import axios from 'axios'

// Este e o nosso "telefone" central para falar com o backend.
// baseURL "/api" e redirecionado para http://localhost:8080 pelo proxy do Vite.
export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})
