<template>
  <v-container>
    <CardDetailsComponent v-bind="card"></CardDetailsComponent>
  </v-container>
</template>


<script setup lang="ts">
import CardDetailsComponent from "@/feature/cards/components/CardDetailsComponent.vue";
import {ref} from "vue";
import {Card} from "@/feature/cards/model/card";
import useCardsService from "@/feature/cards/composables/useCardsService";

const props = defineProps<({
  categoryId: string,
  cardId: string,
})>()

const card = ref<Card>(
  {} as Card
);

async function fetchCardWithId() {
  card.value = await useCardsService().getCardById(props.cardId, props.categoryId);
}

fetchCardWithId();
</script>
