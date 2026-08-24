import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwind from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwind()],
  server: {
    port: 5173,
    // Without this Vite silently falls forward to the next free port when
    // 5173 is taken — which is 5174, the API's port. It binds [::1] while
    // Fastify binds 127.0.0.1, so `localhost:5174` then resolves to Vite
    // first and every API call returns a 500 from the wrong server.
    // Failing loudly on a busy port is far easier to debug.
    strictPort: true,
    proxy: { '/api': 'http://127.0.0.1:5174' },
  },
})
