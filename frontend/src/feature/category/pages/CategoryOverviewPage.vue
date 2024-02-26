<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" fill-height color="containerBackground">
      <v-card-title v-text="'Your accessible categories'" class="text-center text-h4"/>
      <category-iterator :categories="items" :items-per-page="4" :total="totalItems" @reload="fetchCategories"
      @loadNext="(page) => fetchNextPageFromServer(page)" @loadCount="(id) => loadCount(id)"/>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {Category} from "@/feature/category/model/category";
import CategoryIterator from "@/feature/category/components/CategoryIterator.vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import useCardsService from "@/feature/cards/composables/useCardsService";

const items = ref([] as
  { category: Category, expanded: boolean }[]
);
const totalItems = ref(0);

onMounted(async () => fetchCategories());

const fetchCategories = async () => {
  const newItems = [];
  const page = await useCategoriesService().getCategories();
  totalItems.value = page.totalElements;
  for (const category of page.categories) {
    newItems.push({category: category, expanded: false});
  }
  items.value = newItems;
};

const fetchNextPageFromServer = async (page: number) => {
  const pageRes = await useCategoriesService().getCategories(page);
  const newItems = [];
  for (const category of pageRes.categories) {
    newItems.push({category: category, expanded: false});
  }
  items.value = items.value.concat(newItems);
};

const loadCount = async (id: string) => {
  const count = await useCardsService().getCardCount(id);
  const index = items.value.findIndex((item) => item.category.id === id);
  items.value[index].category.numberOfCards = count;
};
</script>
