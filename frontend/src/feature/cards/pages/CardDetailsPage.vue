<template>
  <v-container>
    <div v-if="!Object.keys(card).length">
      <p>no card with {{id}}</p>
    </div>
    <div v-else>
      <CardDetailsComponent v-bind="card"></CardDetailsComponent>
    </div>

  </v-container>
</template>


<script setup lang="ts">
import CardDetailsComponent from "@/feature/cards/components/CardDetailsComponent.vue";
import {ref} from "vue";
import {Card} from "@/feature/cards/model/card";
import useCardsService from "@/feature/cards/composables/useCardsService";

const CATEGORY_ID = "65b7f591f51c4b418123768e";

const props = defineProps({
  id: String,
})

const card = ref<Card>(
  {} as Card
);

async function fetchCardWithId() {
  card.value = await useCardsService().getCardById(props.id!, CATEGORY_ID);
}

fetchCardWithId();

</script>
<style scoped>
</style>
