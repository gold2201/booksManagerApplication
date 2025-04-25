/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", // захватывает все твои компоненты
  ],
  theme: {
    extend: {
      maxWidth: {
        '3xl': '48rem',   // поддерживает max-w-3xl
        'xl': '36rem',    // поддерживает max-w-xl
      },
    },
  },
  plugins: [],
}


