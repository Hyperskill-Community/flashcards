<template>
  <v-container>
    <v-responsive class="align-center text-center fill-height">
      <v-img height="300" src="@/assets/logo.svg" />

      <div class="text-body-2 font-weight-light mb-n1">Welcome to</div>

      <h1 class="text-h2 font-weight-bold">Flashcards</h1>

      <div class="py-14" />
    </v-responsive>
  </v-container>

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
import { ref } from 'vue'
import AxiosLineTemp from "@/shared/_temptestutils/AxiosLineTemp.vue";

const cardId = ref('')

const rules = [
  (id: string) => {
    if (id) return true
    return 'You must enter an id.'
  },
]
</script>
