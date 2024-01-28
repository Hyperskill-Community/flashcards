<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center">
      <h4>{{ category.name }}</h4>
      <div>
        <v-btn variant="text"
               @click="openCategory()"
               icon="mdi-open-in-app"
               size="x-large"/>
        <v-btn variant="text"
               icon="mdi-shape-rectangle-plus"
               :disabled="!putUri"
               @click="addCategory()"
               size="x-large" />
        <v-btn variant="text"
               icon="mdi-square-edit-outline"
               :disabled="!putUri"
               @click="editCategory()"
               size="x-large" />
        <v-btn variant="text"
               :disabled="!!deleteUri"
               @click="deleteCategory()"
               icon="mdi-trash-can-outline"
               size="x-large" />
      </div>
    </v-card-title>

    <v-card-text>
      {{ category.description }}
    </v-card-text>

    <div class="px-4">
      <v-switch
        :model-value="expanded"
        :label="`Show details`"
        :color="`${expanded ? '#43A047' : '#EEEEEE'}`"
        density="compact"
        inset
        @click="() => emit('update:expanded', !expanded)"
      ></v-switch>
    </div>

    <v-divider></v-divider>

    <v-expand-transition>
      <div v-if="expanded && !addRequested && ! editRequested">
        <v-list density="compact" :lines="false">
          <v-list-item :title="`🔥 Your access: ${getAccess(category)}`"></v-list-item>
          <v-list-item :title="`🍔 #Cards in Category: ${category.numberOfCards}`"></v-list-item>
          <v-list-item :title="`🧲 Id: ${category.id}`"></v-list-item>
        </v-list>
      </div>
      <div v-if="addRequested || editRequested" class="d-flex justify-space-between align-center">
        <v-text-field v-model="categoryName" label="Enter category name">
        </v-text-field>
        <v-btn border @click="performUpdate(categoryName)" variant="text">
          Submit
        </v-btn>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script setup lang="ts">
import {Category} from "@/feature/category/model/category";
import {getAccess, getDeleteUri, getPutUri} from "@/feature/category/composables/useCategory";
import {useRouter} from "vue-router";
import {ref} from "vue";
import categoryService from "@/feature/category/composables/useCategoriesService";

const props = defineProps<({
  category: Category,
  expanded: boolean
})>();
const emit = defineEmits<{
  'update:expanded': [val: boolean]
}>();

const putUri = getPutUri(props.category);
const deleteUri = getDeleteUri(props.category);
const addRequested = ref(false);
const editRequested = ref(false);
const categoryName = ref("");

const router = useRouter();
const openCategory = () => {
  resetRequests();
  router.push(`/display/${props.category.id}`);
};

const performUpdate = (categoryName: string) => {
  if (addRequested.value) {
    categoryService().postNewCategory(categoryName);
  } else if (editRequested.value) {
    categoryService().putCategory(props.category.id, categoryName);
  }
};

const addCategory = () => {
  resetRequests();
  addRequested.value = true;
  return `/display/${props.category.id}`;
};
const editCategory = () => {
  resetRequests();
  editRequested.value = true;
  return `/display/${props.category.id}`;
};
const deleteCategory = () => {
  resetRequests();
  categoryService().deleteCategory(props.category.id);
};

const resetRequests = () => {
  addRequested.value = false;
  editRequested.value = false;
}
</script>

<style scoped lang="scss">
.v-card {
  background-color: #f0fff0;
}
</style>
