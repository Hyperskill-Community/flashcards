<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="containerBackground" fill-height>
      <v-container :hidden="displayDetails">
        <v-card-title v-text="`Cards in ${categoryName}`" class="text-center text-h4"/>
        <v-form @submit.prevent="filter.set = filter.input">
          <v-text-field v-model="filter.input" label="Filter on title, tags and question" prepend-inner-icon="mdi-magnify"/>
        </v-form>
        <card-item-scroller :categoryId="categoryId" :filter="filter.set" @openCard="openCard"/>
      </v-container>
      <v-container v-if="displayDetails">
        <card-details :card="card" @close="displayDetails = false"/>
      </v-container>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {ref, shallowRef} from "vue";
import {Card} from "@/feature/cards/model/card.ts";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import CardDetails from "@/feature/cards/components/CardDetails.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";
import {useRoute} from "vue-router";

const props = defineProps<({
  categoryId: string
})>();

const categoryName = useRoute().query.name;
const filter = ref({input: "", set: ""});
const displayDetails = shallowRef(false);
const card = ref<Card>({} as Card);

const openCard = async (id: string) => {
  displayDetails.value = true;
  card.value = await useCardsService().getCardById(id, props.categoryId);
};
</script>
