<template>
  <v-data-iterator
    :items="categories"
    item-value="category.name"
  >
    <template v-slot:default="{items}">
      <v-row>
        <v-col
          v-for="item in items" :key="item.raw.category.name"
          cols="12" sm="12" md="6"
        >
          <category-card :category="item.raw.category"
                         v-model:expanded="item.raw.expanded"
          />
        </v-col>
      </v-row>
    </template>
  </v-data-iterator>
</template>

<script setup lang="ts">
import {Category} from "@/feature/category/model/category";
import CategoryCard from "@/feature/category/components/CategoryCard.vue";

defineProps<({
  categories: { category: Category, expanded: boolean }[]
})>();


</script>
