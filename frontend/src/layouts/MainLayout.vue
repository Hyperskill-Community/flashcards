<template>
  <v-app>
    <v-main>
      <router-view/>
      <toast-layer/>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import ToastLayer from "@/layouts/ToastLayer.vue";
</script>

<style scoped lang="scss">
</style>
