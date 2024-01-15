<template>
  <h2>Card Details Page for card with Id: {{ props.id }}</h2>
  <router-link to="/welcome">Go Home</router-link>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" max-width="500px" min-height="300px"
            color="#E0F7FA">
      <v-card-title class="text-center text-sm-h3">
        Card '{{ props.id }}'
      </v-card-title>
      <v-card-subtitle>
        sport, math, programming
      </v-card-subtitle>
      <v-card-text>
        Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
        Aenean commodo ligula eget dolor. Aenean massa.
        Cum sociis natoque penatibus et
      </v-card-text>
      <v-container>
        <v-card-actions>
          <v-btn class="bg-blue-accent-1 font-weight-medium">Edit</v-btn>
          <v-btn class="bg-blue-accent-1 font-weight-medium">Delete</v-btn>
        </v-card-actions>
      </v-container>
    </v-card>
  </v-container>
</template>


<script setup lang="ts">
const props = defineProps({
  id: String
})
</script>

<style scoped>

</style>
