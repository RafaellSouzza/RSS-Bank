/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  darkMode: 'class', 
  theme: {
    extend: {
      colors: {
        darkBg: '#121212', 
        darkCard: '#1E1E1E', 
        darkText: '#E0E0E0', 
        primary: '#BB86FC', 
        secondary: '#03DAC6', 
      },
      fontFamily: {
        sans: ['Orbitron', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
