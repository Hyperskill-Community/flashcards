<template>
  <base-card-page>
    <v-img height="300" src="@/assets/logo.svg"/>
    <audio autoplay ref="audioRef">
      <source src="../../assets/Welcome_Serena.mp3" type="audio/mpeg">
    </audio>
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
import {onMounted, ref} from "vue";

const audioRef = ref({} as HTMLAudioElement);

onMounted(() => {
  audioRef.value.volume = 0.1; // 10% volume
});

</script>
