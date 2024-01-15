<template>
  <v-app>
    <v-main>
      <v-navigation-drawer color="#F0F4C3">
        <v-list-item title="Flashcards" subtitle="Navigation"></v-list-item>
        <v-divider></v-divider>
        <v-list-item to="/categories" title="Your card categories"></v-list-item>
        <v-list-item to="/display/sample" title="Card detail sample"></v-list-item>
        <v-list-item to="/test" title="Test axios"></v-list-item>
        <v-divider></v-divider>
        <v-list-item to="/welcome" title="Welcome"></v-list-item>
      </v-navigation-drawer>
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
