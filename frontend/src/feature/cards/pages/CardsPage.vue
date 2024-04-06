<template>
  <v-container :hidden="uiState.display !== 'cards'">
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="secondary" fill-height>
      <v-card-title v-text="`Cards in ${categoryName}`" class="text-center text-h4"/>
      <v-row class="mr-0">
        <v-col md="7"/>
        <v-col cols="12" md="3">
            <v-select v-if="uiState.selectActive" v-model="cardType" label="Select type of new card"
                      :items="[CardType.SIMPLEQA, CardType.MULTIPLE_CHOICE, CardType.SINGLE_CHOICE]"/>
        </v-col>
        <v-col md="1"/>
        <v-col cols="12" md="1" class="mb-4">
          <add-mdi-button tooltip-text="Create new card" :click-handler="addButtonClicked"/>
        </v-col>
      </v-row>

      <v-form @submit.prevent="filter.set = filter.input">
        <v-text-field clearable @click:clear="filter.input=''" v-model="filter.input"
                      label="Filter on title, tags and question" prepend-inner-icon="mdi-magnify"/>
      </v-form>
      <card-item-scroller :categoryId="categoryId" :filter="filter.set" :reload="toggleReload" @openCard="openCard"/>
    </v-card>
  </v-container>

  <card-details v-if="uiState.display === 'details'" :card="card"
                @close="uiState.display = 'cards'" @edit="editForm" @delete="deleteCard"/>
  <card-form v-if="uiState.display === 'form'" :mode="uiState.formMode" :card="card"
             @close="closeForm" @update="updateCard" @post="addCard"/>
</template>

<script setup lang="ts">
import {ref, shallowRef} from "vue";
import {useRoute} from "vue-router";
import CardDetails from "@/feature/cards/components/CardDetails.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";
import AddMdiButton from "@/shared/buttons/AddMdiButton.vue";
import CardForm from "@/feature/cards/components/CardForm.vue";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import {Card, CardType, emptyCard} from "@/feature/cards/model/card.ts";

const props = defineProps<({
  categoryId: string
})>();

const categoryName = useRoute().query.name;
const filter = ref({input: "", set: ""});
const uiState = ref<{
  display: 'cards' | 'form' | 'details', formMode: 'edit' | 'add', selectActive: boolean
}>({
  display: 'cards',
  formMode: 'edit',
  selectActive: false
});
const toggleReload = shallowRef(false);
const card = ref<Card>({} as Card);
const cardType = ref<CardType>(CardType.SIMPLEQA);

const openCard = async (id: string) => {
  uiState.value.display = 'details';
  card.value = await useCardsService().getCardById(id, props.categoryId);
};

const addButtonClicked = () => {
  // If no card type selection shown, show it, else open add form with empty card of selected type
  if (uiState.value.selectActive) {
    card.value = emptyCard(cardType.value);
    uiState.value.formMode = 'add';
    uiState.value.display = 'form';
  }
  uiState.value.selectActive = !uiState.value.selectActive;
};

const addCard = async (newCard: Card) => {
  await useCardsService().postNewCard(props.categoryId, newCard);
  reloadAndShowCards();
};

const updateCard = async (newCard: Card) => {
  newCard.tags = newCard.tags.filter(tag => !!tag);
  card.value = await useCardsService().putCard(props.categoryId, newCard);
  reloadAndShowCards();
};

const deleteCard = async (curCard: Card) => {
  await useCardsService().deleteCard(props.categoryId, curCard);
  reloadAndShowCards();
};

const reloadAndShowCards = () => {
  toggleReload.value = !toggleReload.value;
  uiState.value.display = 'cards';
};

const editForm = () => {
  uiState.value.formMode = 'edit';
  uiState.value.display = 'form';
};

const closeForm = () => {
  uiState.value.formMode === 'edit' ?
    uiState.value.display = 'details' :
    uiState.value.display = 'cards';
};
</script>
