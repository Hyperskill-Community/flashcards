<template>
  <v-container>
    <div v-if="!error">
      <CardDetailsComponent v-bind="card"></CardDetailsComponent>
    </div>
    <div v-else>
      <p class="no-card-found">{{ error }}</p>
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

const card = ref<Card | null>({
  id: "a",
  title: "title",
  question: "what is title",
  correctOption: "",
  tags: [],
  options: []
});

const error = ref(null);

async function fetchCardWithId() {
  try {
    card.value = await useCardsService().getCardById(props.id!, CATEGORY_ID);
    error.value = null;
  } catch (e: any) {
    error.value = e.message;
  }
}

fetchCardWithId();
</script>

<style scoped>
.no-card-found {
  text-align: center;
  font-weight: bold;
}
</style>
