<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="containerBackground" fill-height>
      <v-container :hidden="displayDetails">
        <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="containerBackground" fill-height>
          <v-card-title v-text="`Cards in ${categoryName}`" class="text-center text-h4"/>
          <v-form @submit.prevent class="d-flex justify-space-between align-center">
            <v-text-field v-model="filter.input"
                          label="Filter on title, tags and question"
                          prepend-inner-icon="mdi-magnify"/>
            <submit-mdi-button :clickHandler="() => filter.set = filter.input" :disabled="!filter.input"/>
          </v-form>
          <card-item-scroller :categoryId="categoryId" :filter="filter.set"
                              @openCard="(cardId) => openCard(cardId)"/>
        </v-card>
      </v-container>
      <v-container v-if="displayDetails">
        <card-details :card="card" @close="() => displayDetails = false"/>
      </v-container>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {onMounted, ref, shallowRef} from "vue";
import {Card} from "@/feature/cards/model/card.ts";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import CardDetails from "@/feature/cards/components/CardDetails.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";
import SubmitMdiButton from "@/shared/components/SubmitMdiButton.vue";

const props = defineProps<({
  categoryId: string
})>();
const categoryName = shallowRef("");
const filter = ref({input: "", set: ""});
const displayDetails = shallowRef(false);
const card = ref<Card>({} as Card);

const openCard = async (id: string) => {
  displayDetails.value = true;
  card.value = await useCardsService().getCardById(id, props.categoryId);
};

onMounted(async () => {
  categoryName.value = (await useCategoriesService().getCategoryById(props.categoryId)).name;
});
</script>

