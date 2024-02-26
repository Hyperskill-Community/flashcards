<template>
  <v-card color="secondary">
    <v-card-title class="d-flex justify-space-between align-center">
      <h4 v-text="category.name"/>
      <div class="mr-n6">
        <!--  ms-n6  = negative margin 3 on sides - also ml, mr, ma, mt, mb -->
        <open-mdi-button tooltip-text="Open Category"
                         :clickHandler="openCategory"/>
        <edit-mdi-button tooltip-text="Rename Category"
                         :disabled="!putUri"
                         :clickHandler="editCategory"/>
        <delete-mdi-button tooltip-text="Delete Category"
                           :disabled="!deleteUri"
                           :clickHandler="deleteCategory"/>
      </div>
    </v-card-title>
    <v-card-text v-text="category.description?? 'No description given'"/>
    <div class="px-4">
      <v-switch
        :model-value="expanded"
        :label="`Show details`"
        :color="toggleColor"
        density="compact"
        inset
        @click="() => emit('update:expanded', !expanded)"
      />
    </div>
    <v-divider/>
    <v-expand-transition>
      <div v-if="expanded && !editRequested">
        <v-list density="compact" :lines="false">
          <v-list-item :title="`🔥 Your access: ${getAccess(category)}`"/>
          <v-list-item :title="`🍔 #Cards in Category: ${category.numberOfCards}`"/>
          <v-list-item :title="`🧲 Id: ${category.id}`"/>
        </v-list>
      </div>
      <v-container v-if="editRequested">
        <v-form @submit.prevent class="d-flex justify-space-between align-center">
          <v-text-field density="compact" v-model="updateRequest.name" label="Category name" />
          <v-text-field density="compact" v-model="updateRequest.description" label="Description" />
          <submit-mdi-button :disabled="!updateRequest.name && ! updateRequest.description"
                             :clickHandler="performUpdate"/>
        </v-form>
      </v-container>
    </v-expand-transition>
  </v-card>
</template>

<script setup lang="ts">
import {Category} from "@/feature/category/model/category";
import {getAccess, getDeleteUri, getPutUri} from "@/feature/category/composables/useCategory";
import {useRouter} from "vue-router";
import {computed, ref} from "vue";
import categoryService from "@/feature/category/composables/useCategoriesService";
import OpenMdiButton from "@/shared/components/OpenMdiButton.vue";
import DeleteMdiButton from "@/shared/components/DeleteMdiButton.vue";
import EditMdiButton from "@/shared/components/EditMdiButton.vue";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";

const props = defineProps<({
  category: Category,
  expanded: boolean
})>();

const emit = defineEmits<{
  'update:expanded': [val: boolean]
  'reload': [val: boolean]
}>();

const router = useRouter();
const putUri = getPutUri(props.category);
const deleteUri = getDeleteUri(props.category);
const editRequested = ref(false);
const updateRequest = ref({name: "", description: ""});

const toggleColor = computed(() => props.expanded ? '#43a047' : '#eeeeee');

const openCategory = () => {
  router.push(`/category/${props.category.id}`);
};

const performUpdate = async () => {
  await categoryService().putCategory(props.category.id, updateRequest.value);
  emit('reload', true);
};

const editCategory = () => {
  editRequested.value = !editRequested.value;
};

const deleteCategory = async () => {
  await categoryService().deleteCategory(props.category.id);
  emit('reload', true);
};
</script>
