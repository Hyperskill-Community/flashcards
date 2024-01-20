<template>
  <v-container>
    <h2 class="text-center">Card Details Page for card with Id: {{ props.id }}</h2>
    <CardDetailsComponent></CardDetailsComponent>
  </v-container>
</template>


<script setup lang="ts">
import CardDetailsComponent from "@/feature/cardmanagement/components/CardDetailsComponent.vue";

const props = defineProps({
  id: String
})
</script>

<style scoped>

</style>
