<template>
  <v-card
    class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" max-width="500px" min-height="300px">
    <v-card-title>
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
      <ol>
        <li v-for="(option, index) in card.options">
          {{ index }}./{{ option }}
        </li>
      </ol>
      <p>
        Answer: {{ card.correctOption }}
      </p>
    </v-card-text>
    <v-container>
      <v-card-actions class="pa-0 ma-0">
        <edit-mdi-button>Edit</edit-mdi-button>
        <delete-mdi-button>Delete</delete-mdi-button>
      </v-card-actions>
    </v-container>
  </v-card>
</template>

<script setup lang="ts">

import EditMdiButton from "@/shared/components/EditMdiButton.vue";
import DeleteMdiButton from "@/shared/components/DeleteMdiButton.vue";

const card = defineProps({
  id: String,
  title: String,
  question: String,
  correctOption: String,
  tags: Array as () => String[],
  options: Array as () => String[]
});

</script>

<style scoped lang="scss">

ul, li {
  margin: 0;
  padding: 0;
  list-style: none;
}

.tags {
  gap: 0.5rem;
  display: flex;
}

.v-card {
  background-color: #f0f8ff;
}

</style>
