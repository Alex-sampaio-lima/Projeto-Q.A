import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globals: true,
    // Troca jsdom por happy-dom para evitar o erro de ESM/CJS com @asamuzakjp/css-color
    environment: 'happy-dom',
    pool: 'threads',
    threads: {
      singleThread: true,
    },
    isolate: false,
    fileParallelism: false,
    reporters: ['verbose'],
    testTimeout: 60000,
  },
});
