<template>
  <v-card
    class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" max-width="500px" min-height="300px">
    <v-card-title class="text-center">
      {{ card.title }}
    </v-card-title>
    <v-card-subtitle>
      <ul class="tags">
        <li v-for="(tag, index) in card.tags">
          <template v-if="index < card.tags!.length-1 ">
            {{ tag }},
          </template>
          <template v-else>
            {{ tag }}
          </template>
        </li>
      </ul>
    </v-card-subtitle>
    <v-card-text>
      <p>
        {{ card.question }}
      </p>
      <p>
        Answer: {{ card.correctOption }}
      </p>
    </v-card-text>
    <v-container>
      <v-card-actions class="pa-0 ma-0">
        <v-btn class="bg-blue-accent-1 font-weight-medium">Edit</v-btn>
        <v-btn class="bg-blue-accent-1 font-weight-medium">Delete</v-btn>
      </v-card-actions>
    </v-container>
  </v-card>
</template>

<script setup lang="ts">

const card = defineProps({
  id: String,
  title: String,
  question: String,
  correctOption: String,
  tags: Array as () => String[],
});

</script>

<style scoped lang="scss">
.tags {
  gap: 0.5rem;
  display: flex;
  margin: 0;
  padding: 0;
  list-style: none;
}

</style>
