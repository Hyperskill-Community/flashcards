<template>
  <v-navigation-drawer :color="primary()">
    <v-list-item title="Flashcards" subtitle="Menu"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/categories" title="Your card categories"></v-list-item>
    <v-list-item to="/card/cat/sample" title="Card detail sample"></v-list-item>
    <v-list-item to="/test" title="Test axios"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/" title="Welcome"></v-list-item>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import {primary} from "@/styles/currentTheme";
</script>

<style scoped lang="scss">
</style>
