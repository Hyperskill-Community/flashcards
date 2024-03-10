<template>
  <v-data-iterator :items="categories" item-value="category.name" :items-per-page="itemsPerPage" :page="pageRef.page">
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
                         @loadCount="emit('loadCount', $event)"
                         @reload="emit('reload', true);"/>
        </v-col>
      </v-row>
    </template>
    <template v-slot:footer="{ page }">
      <pagination-footer :total-pages="totalPages" :page="page"
                         @update:page="newVal => paginate(newVal)"/>
    </template>
  </v-data-iterator>
</template>

<script setup lang="ts">
import {Category, CategoryRequest} from "@/feature/category/model/category";
import CategoryCard from "@/feature/category/components/CategoryCard.vue";
import AddMdiButton from "@/shared/buttons/AddMdiButton.vue";
import {computed, ref, watch} from "vue";
import SubmitMdiButton from "@/shared/buttons/SubmitMdiButton.vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import PaginationFooter from "@/shared/components/PaginationFooter.vue";

const props = defineProps<({
  categories: { category: Category, expanded: boolean }[],
  itemsPerPage: number,
  total: number
})>();

const emit = defineEmits<{
  'reload': [val: boolean],
  'loadNext': [page: number],
  'loadCount': [categoryId: string]
}>();

const addRequested = ref(false);
const newCategory = ref<CategoryRequest>({} as CategoryRequest);
const pageRef = ref({page: 1, loadNext: false});

const totalPages = computed(() => pageCount(props.total));

watch(() => props.categories.length, () => {
  if (pageRef.value.loadNext) { // if loadNext set, we just loaded new items and can now jump to requested page
    pageRef.value.loadNext = false;
    pageRef.value.page++;
  } else { // after reload, we may have fewer items, so we need to adjust the page number to the highest available
    pageRef.value.page = Math.min(pageRef.value.page, pageCount(props.categories.length));
  }
});

const paginate = (pageRequested: number) => {
  if ((pageRequested - 1) * props.itemsPerPage == props.categories.length) {
    // if we are at the end of the list, emit loadNext to load next page from server,
    // increment pageRef.page is deferred until data is loaded
    emit('loadNext', pageRef.value.page * props.itemsPerPage / 20); // 20 pages in server response
    pageRef.value.loadNext = true;
  } else {
    pageRef.value.page = pageRequested;
  }
};

const addCategory = () => {
  newCategory.value = {name: "", description: ""};
  addRequested.value = !addRequested.value;
};

const postNewCategory = async () => {
  addRequested.value = false;
  await useCategoriesService().postNewCategory(newCategory.value);
  emit('reload', true);
};

// calcs amount of pages needed for given amount of items
const pageCount = (itemCount: number) => {
  return Math.ceil(itemCount / props.itemsPerPage);
};
</script>
