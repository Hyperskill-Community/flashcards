<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between"
            fill-height
            color="containerBackground">
      <v-card-title class="text-center text-h4">
        Your accessible categories
      </v-card-title>
      <category-iterator :categories="items" @reload="fetchCategories"/>
    </v-card>
  </v-container>
</template>


<script setup lang="ts">
import {ref} from "vue";
import {Category} from "@/feature/category/model/category";
import CategoryIterator from "@/feature/category/components/CategoryIterator.vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import useCardsService from "@/feature/cards/composables/useCardsService";

const items = ref([] as
  { category: Category, expanded: boolean }[]
);

// ------------------- TEST DATA -------------------
// ---uncomment if on npm run mode - and comment out the fetchCategories() call
// items.value.push({category: {id: "123", name: "test", description: "test", numberOfCards: 1, actions:
//   [
//     {
//       "action": ActionType.WRITE,
//       "uri": "http://localhost:8080/categories/123"
//     },
//     {
//       "action": ActionType.DELETE,
//       "uri": "http://localhost:8080/categories/123"
//     }
//   ]
//   }, expanded: false});
//
// items.value.push({category: {id: "1234", name: "test2", description: "test description", numberOfCards: 2, actions:
//       [
//         {
//           "action": ActionType.READ,
//           "uri": "http://localhost:8080/categories/123"
//         },
//         {
//           "action": ActionType.DELETE,
//           "uri": "http://localhost:8080/categories/123"
//         }
//       ]
//   }, expanded: false});
//
// items.value.push({category: {id: "12345", name: "test3", description: "test with blah", numberOfCards: 3, actions:
//       [
//         {
//           "action": ActionType.WRITE,
//           "uri": "http://localhost:8080/categories/123"
//         },
//         {
//           "action": ActionType.READ,
//           "uri": "http://localhost:8080/categories/123"
//         }
//       ]
//   }, expanded: false});
// items.value.push({category: {id: "123456", name: "test-last", description: "test", numberOfCards: 2, actions:
//       [
//         {
//           "action": ActionType.READ,
//           "uri": "http://localhost:8080/categories/123"
//         },
//         {
//           "action": ActionType.WRITE,
//           "uri": "http://localhost:8080/categories/123"
//         },
//         {
//           "action": ActionType.DELETE,
//           "uri": "http://localhost:8080/categories/123"
//         }
//       ]
//   }, expanded: false});

const fetchCategories = async () => {
  items.value = [];
  const categories = await useCategoriesService().getCategories();
  for (const category of categories) {
    items.value.push({category: category, expanded: false});
    category.numberOfCards = await useCardsService().getCardCount(category.id);
  }
}
fetchCategories();
</script>
