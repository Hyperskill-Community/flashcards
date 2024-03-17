<template>
  <v-card color="secondary">
    <v-card-title class="ml-3">
      <h4 v-text="category.name"/>
    </v-card-title>

    <v-card-text class="ml-3">
      {{ category.description || "No description available" }}
    </v-card-text>

      <v-card-actions class="ma-4">
        <v-switch :model-value="expanded" label="Show details"
                  :color="toggleColor" :disabled="editRequested"
                  density="compact" inset @click="expand"/>
        <v-spacer/>
        <open-mdi-button tooltip-text="Open Category"
                         :clickHandler="openCategory"/>
        <edit-mdi-button tooltip-text="Rename Category"
                         :disabled="!putUri"
                         :clickHandler="editCategory"/>
        <delete-mdi-button tooltip-text="Delete Category"
                           :disabled="!deleteUri"
                           :clickHandler="deleteCategory"/>
      </v-card-actions>
    <v-divider/>
    <v-container v-if="editRequested">
      <v-form @submit.prevent class="d-flex justify-space-between align-center">
        <v-text-field density="compact" v-model="updateRequest.name" label="Category name"/>
        <v-text-field density="compact" v-model="updateRequest.description" label="Description"/>
        <submit-mdi-button :disabled="!updateRequest.name && ! updateRequest.description"
                           :clickHandler="performUpdate"/>
      </v-form>
    </v-container>
    <v-expand-transition>
      <div v-if="expanded && !editRequested">
        <v-list density="compact" :lines="false">
          <v-list-item :title="`🔥 Your access: ${getAccess(category)}`"/>
          <v-list-item :title="`🍔 #Cards in Category: ${category.numberOfCards}`"/>
          <v-list-item :title="`🧲 Id: ${category.id}`"/>
        </v-list>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script setup lang="ts">
import {Category} from "@/feature/category/model/category";
import {getAccess, getDeleteUri, getPutUri} from "@/feature/category/composables/useCategory";
import {useRouter} from "vue-router";
import {computed, ref} from "vue";
import categoryService from "@/feature/category/composables/useCategoriesService";
import OpenMdiButton from "@/shared/buttons/OpenMdiButton.vue";
import DeleteMdiButton from "@/shared/buttons/DeleteMdiButton.vue";
import EditMdiButton from "@/shared/buttons/EditMdiButton.vue";
import SubmitMdiButton from "@/shared/buttons/SubmitMdiButton.vue";

const props = defineProps<({
  category: Category,
  expanded: boolean
})>();

const emit = defineEmits<{
  'update:expanded': [val: boolean],
  'loadCount': [categoryId: string],
  'reload': [val: boolean]
}>();

const router = useRouter();
const putUri = getPutUri(props.category);
const deleteUri = getDeleteUri(props.category);
const editRequested = ref(false);
const updateRequest = ref({name: props.category.name, description: props.category.description!});

const toggleColor = computed(() => props.expanded ? '#43a047' : '#eeeeee');

const expand = () => {
  // if props.expanded is false, emit loadCount to load card count from server
  props.expanded || emit('loadCount', props.category.id);
  emit('update:expanded', !props.expanded);
};

const openCategory = () => router.push(`/category/${props.category.id}?name=${props.category.name}`);

const performUpdate = async () => {
  await categoryService().putCategory(props.category.id, updateRequest.value);
  emit('reload', true);
};

const editCategory = () => editRequested.value = !editRequested.value;

const deleteCategory = async () => {
  await categoryService().deleteCategory(props.category.id);
  emit('reload', true);
};
</script>
