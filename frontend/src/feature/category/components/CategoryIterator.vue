<template>
  <v-data-iterator :items="categories" item-value="category.name" items-per-page="6">
    <template v-slot:header>
      <v-row>
        <v-col cols="12" sm="12" md="11">
          <v-form v-if="addRequested" @submit.prevent class="d-flex align-center">
            <v-text-field v-model="newCategory.name" label="Category name" class="v-col-sm-4"/>
            <v-text-field v-model="newCategory.description" label="Description (optional)" class="v-col-sm-6"/>
            <submit-mdi-button :disabled="!newCategory.name" :clickHandler="postNewCategory"/>
          </v-form>
        </v-col>
        <v-col cols="12" sm="12" md="1">
          <add-mdi-button :click-handler="addCategory" tooltipText="Create new category"/>
        </v-col>
      </v-row>
    </template>
    <template v-slot:default="{items}">
      <v-row>
        <v-col v-for="item in items" :key="item.raw.category.name" cols="12" sm="12" md="6">
          <category-card :category="item.raw.category"
                         v-model:expanded="item.raw.expanded"
                         @reload="emit('reload', true)"/>
        </v-col>
      </v-row>
    </template>
  </v-data-iterator>
</template>

<script setup lang="ts">
import {Category, CategoryRequest} from "@/feature/category/model/category";
import CategoryCard from "@/feature/category/components/CategoryCard.vue";
import AddMdiButton from "@/shared/components/AddMdiButton.vue";
import {ref} from "vue";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";

defineProps<({
  categories: { category: Category, expanded: boolean }[],
})>();

const emit = defineEmits<{
  'reload': [val: boolean]
}>();

const addRequested = ref(false);
const newCategory = ref<CategoryRequest>({} as CategoryRequest);

const addCategory = () => {
  newCategory.value = {name: "", description: ""};
  addRequested.value = !addRequested.value;
};

const postNewCategory = async () => {
  addRequested.value = false;
  await useCategoriesService().postNewCategory(newCategory.value);
  emit('reload', true);
};
</script>
