<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between"
            fill-height
            :color="containerBackground()">
      <v-card-title class="text-center text-h4">
        Cards in {{ categoryName }}
      </v-card-title>
      <v-form @submit.prevent class="d-flex justify-space-between align-center">
        <v-text-field label="Filter on title or tags"
                      prepend-inner-icon="mdi-magnify"
                      v-model="filter">
        </v-text-field>
        <submit-mdi-button :disabled="!filter"
                           :clickHandler="filterOnTitle"/>
      </v-form>
      <card-item-scroller :categoryId="categoryId" :filter="filter"/>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {containerBackground} from "@/styles/currentTheme";
import {ref} from "vue";
import useCategoriesService from "@/feature/category/composables/useCategoriesService";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";

const props = defineProps<({
  categoryId: string
})>();
const categoryName = ref("");
const filter = ref("");
const filterOnTitle = () => {
  console.log("Filtering on title: " + filter.value);
}
const fetchCategoryName = async () => {
  categoryName.value = (await useCategoriesService().getCategoryById(props.categoryId)).name;
}
fetchCategoryName();
</script>

