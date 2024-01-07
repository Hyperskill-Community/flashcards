<template>
  <HelloWorld />
  <v-sheet width="230" class="mx-auto">
    <v-form @submit.prevent>
      <v-text-field
        v-model="cardId"
        :rules="rules"
        label="Card Id"
      ></v-text-field>
      <router-link :to="`/display/${cardId}`">
        View card details
      </router-link>
    </v-form>
  </v-sheet>
</template>

<script lang="ts" setup>
import HelloWorld from '@/shared/components/HelloWorld.vue'
import { ref } from 'vue'

const cardId = ref('')

const rules = [
  (id: string) => {
    if (id) return true
    return 'You must enter an id.'
  },
]
</script>
