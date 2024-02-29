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
      <v-container v-if="totalPages > 1" class="d-flex align-center justify-center pa-4">
        <v-btn :disabled="page === 1" icon="mdi-chevron-left" density="comfortable" variant="tonal" rounded
               @click="() => pageRef.page--"
        ></v-btn>
        <div class="mx-2 text-caption">
          Page {{ page }} of {{ totalPages }}
        </div>
        <v-btn :disabled="page >= totalPages" icon="mdi-chevron-right" density="comfortable" variant="tonal" rounded
               @click="() => pageForward(page)"
        ></v-btn>
      </v-container>
    </template>
  </v-data-iterator>
</template>

<script setup lang="ts">
import {Category, CategoryRequest} from "@/feature/category/model/category";
import CategoryCard from "@/feature/category/components/CategoryCard.vue";
import AddMdiButton from "@/shared/components/AddMdiButton.vue";
import {computed, ref, watch} from "vue";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";

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
  } else { // after reload we might have less items, so we need to adjust the page number to the highest available
    pageRef.value.page = Math.min(pageRef.value.page, pageCount(props.categories.length));
  }
});

const addCategory = () => {
  newCategory.value = {name: "", description: ""};
  addRequested.value = !addRequested.value;
};

const postNewCategory = async () => {
  addRequested.value = false;
  await useCategoriesService().postNewCategory(newCategory.value);
  emit('reload', true);
};

const pageForward = async (page: number) => {
  if (page * props.itemsPerPage == props.categories.length) {
    // if we are at the end of the list, emit loadNext to load next page from server,
    // increment pageRef.page is deferred until data is loaded
    emit('loadNext', page * props.itemsPerPage / 20); // 20 pages in server response
    pageRef.value.loadNext = true;
  } else {
    pageRef.value.page++;
  }
};

// calcs amount of pages needed for given amount of items
const pageCount = (itemCount: number) => {
  return Math.ceil(itemCount / props.itemsPerPage);
};
</script>
