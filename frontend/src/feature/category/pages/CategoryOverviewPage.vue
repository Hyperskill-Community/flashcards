<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" fill-height color="#EEEEEE">
      <v-card-title class="text-center">
        Your categories
      </v-card-title>
      <CategoryIterator :categories="categories"/>
    </v-card>
  </v-container>
</template>


<script setup lang="ts">
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import {ref} from "vue";
import {Category} from "@/feature/category/model/category";
import CategoryIterator from "@/feature/category/components/CategoryIterator.vue";
import useCardsService from "@/feature/cards/composables/useCardsService";

const categories = ref([] as Category[]);

const fetchCategories = async () => {
  categories.value = await useCategoriesService().getCategories();
  for (const category of categories.value) {
    category.numberOfCards = await useCardsService().getCardCount(category.id);
  }
}
fetchCategories();
</script>