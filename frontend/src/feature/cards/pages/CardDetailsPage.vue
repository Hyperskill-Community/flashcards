<template>
  <v-container>
    <card-details-component :card="card"></card-details-component>
  </v-container>
</template>


<script setup lang="ts">
import CardDetailsComponent from "@/feature/cards/components/CardDetailsComponent.vue";
import {onMounted, ref} from "vue";
import {Card} from "@/feature/cards/model/card";
import useCardsService from "@/feature/cards/composables/useCardsService";

const props = defineProps<({
  categoryId: string,
  cardId: string,
})>()

const card = ref<Card>(
  {} as Card
);

onMounted(async () => {
  card.value = await useCardsService().getCardById(props.cardId, props.categoryId);
});

</script>
