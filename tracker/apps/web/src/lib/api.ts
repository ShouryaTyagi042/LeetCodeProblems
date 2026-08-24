import { createApi } from '@tracker/shared'

// Vite proxies /api to the Fastify server in dev. For a build, set
// VITE_API_URL. React Native will build its own client with a LAN or host
// URL -- this file is the only thing that differs between the two apps.
export const api = createApi({
  baseUrl: import.meta.env.VITE_API_URL ?? '',
  token: import.meta.env.VITE_API_TOKEN,
})
