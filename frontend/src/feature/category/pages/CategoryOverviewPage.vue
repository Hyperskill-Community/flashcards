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

const fetchCategories = async () => {
  items.value = [];
  const categories = await useCategoriesService().getCategories();
  for (const category of categories) {
    items.value.push({category: category, expanded: false});
    category.numberOfCards = await useCardsService().getCardCount(category.id);
  }
};
fetchCategories();
</script>
