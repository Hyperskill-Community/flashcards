<template>
  <h2>Card Details Page for card with Id: {{ props.id }}</h2>
  <router-link to="/">Go Home</router-link>
</template>

<script setup lang="ts">
const props = defineProps({
  id: String
})
</script>

<style scoped>

</style>
