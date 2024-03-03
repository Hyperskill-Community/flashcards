<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="containerBackground" fill-height>
      <v-card-title v-text="`Cards in ${categoryName}`" class="text-center text-h4"/>
      <v-form @submit.prevent class="d-flex justify-space-between align-center">
        <v-text-field v-model="filter.input"
                      label="Filter on title, tags and question"
                      prepend-inner-icon="mdi-magnify"/>
        <submit-mdi-button :clickHandler="filterOnTitle" :disabled="!filter.input"/>
      </v-form>
      <card-item-scroller :categoryId="categoryId" :titleFilter="filter.title"/>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";

const props = defineProps<({
  categoryId: string
})>();

const categoryName = ref("");
const filter = ref({
  input: "",
  title: "", // more to come
});

onMounted(async () => {
  categoryName.value = (await useCategoriesService().getCategoryById(props.categoryId)).name;
});

const filterOnTitle = () => filter.value.title = filter.value.input;
</script>

