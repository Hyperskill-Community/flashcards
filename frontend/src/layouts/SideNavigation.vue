<template>
  <v-navigation-drawer>
    <v-list-item title="Flashcards" subtitle="Navigation"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/categories" title="Your card categories"></v-list-item>
    <v-list-item to="/display/sample" title="Card detail sample"></v-list-item>
    <v-list-item to="/test" title="Test axios"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/" title="Welcome"></v-list-item>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.v-navigation-drawer {
  background-color: #d8ffd8;
}
</style>
