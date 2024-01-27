<template>
  <v-data-iterator
    :items="its"
    item-value="name"
  >
    <template v-slot:default="{ items}">
      <v-row>
        <v-col
          v-for="item in items"
          :key="item.raw.name"
          cols="12"
          sm="12"
          md="6"
        >
          <category-card :category="item.raw.category"
                         :name="item.raw.name"
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
import {ref} from "vue";

const props = defineProps<({
  categories: Category[]
})>();
const categoriesWithExpand = props.categories.map(category => {
  return {
    name: category.name,
    category: category,
    expanded: false
  }
});
console.log(categoriesWithExpand);
const its = ref(categoriesWithExpand);

</script>
