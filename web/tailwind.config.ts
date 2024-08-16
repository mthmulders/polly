import type { Config } from 'tailwindcss'

const config: Config = {
  content: {
    relative: true,
    files: [
      './src/main/webapp/**/*.html',
      './src/main/webapp/**/*.jsp',
      './src/main/webapp/**/*.tag',
    ]
  },
  theme: {
    container: {
      center: true,
    }
  },
  darkMode: 'class',
  plugins: [
    require("daisyui"),
    require('@tailwindcss/typography'),
  ],
  daisyui: {
    themes: ['dracula']
  }
}
export default config
