import pluginVue from 'eslint-plugin-vue'
import typescripteslint from "typescript-eslint";
import vueParser from "vue-eslint-parser";

export default [
  ...typescripteslint.configs.recommended,
  ...pluginVue.configs['flat/recommended'],
  {
    rules: {
      'vue/no-v-text-v-html-on-component': 'off',
      'vue/attribute-hyphenation': 'off',  // onLoad needed in VInfiniteScroller
    },
    languageOptions: {
      ecmaVersion: 2024,
      parser: vueParser,
      parserOptions: {
        parser: "@typescript-eslint/parser",
        ecmaFeatures: {
          jsx: false
        }
      }
    }
  },
  { ignores: ['node_modules/', 'dist'] }
]
