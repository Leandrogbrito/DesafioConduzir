import axios from 'axios'

// Este e o nosso "telefone" central para falar com o backend.
// baseURL "/api" e redirecionado para http://localhost:8080 pelo proxy do Vite.
export const api = axios.create({
 baseURL: 'http://desafio-conduzir-env.eba-ipu5dsfz.us-east-1.elasticbeanstalk.com',
  headers: { 'Content-Type': 'application/json' },
})
