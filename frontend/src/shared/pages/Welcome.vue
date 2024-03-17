<template>
  <base-card-page>
    <v-img height="300" src="@/assets/logo.svg"/>
    <v-card-text>
      <v-row class="d-flex flex-column align-center pa-0">
        <div v-text="'Welcome to'" class="text-body-2 font-weight-bold"/>
        <div v-text="'Flashcards'" class="text-h2 font-weight-bold mb-5"/>
      </v-row>
    </v-card-text>
  </base-card-page>
</template>
<script setup lang="ts">
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";
</script>
