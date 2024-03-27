<template>
  <v-container :hidden="displayDetails || displayEdit">
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" color="secondary" fill-height>
      <v-card-title v-text="`Cards in ${categoryName}`" class="text-center text-h4"/>
      <v-select
        v-model="cardType"
        label="Select type of new card"
        :items="[CardType.SIMPLEQA, CardType.MULTIPLE_CHOICE, CardType.SINGLE_CHOICE]"
      ></v-select>
      <add-mdi-button tooltip-text="create card" :click-handler="addCard"/>
      <v-form @submit.prevent="filter.set = filter.input">
        <v-text-field clearable @click:clear="filter.input=''" v-model="filter.input"
                      label="Filter on title, tags and question" prepend-inner-icon="mdi-magnify"/>
      </v-form>
      <card-item-scroller :categoryId="categoryId" :filter="filter.set" :reload="toggleReload" @openCard="openCard"/>
    </v-card>
  </v-container>

  <card-details v-if="displayDetails" :card="card" @close="displayDetails = false" @edit="toggleEdit"
                @delete="deleteCard"/>
  <card-edit v-if="displayEdit" :card="card" @close="toggleEdit" @update="updateCard"/>
</template>

<script setup lang="ts">
import {ref, shallowRef} from "vue";
import {Card, CardType} from "@/feature/cards/model/card.ts";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import CardDetails from "@/feature/cards/components/CardDetails.vue";
import CardItemScroller from "@/feature/cards/components/CardItemScroller.vue";
import {useRoute} from "vue-router";
import CardEdit from "@/feature/cards/components/CardEdit.vue";
import AddMdiButton from "@/shared/buttons/AddMdiButton.vue";

const props = defineProps<({
  categoryId: string
})>();

const categoryName = useRoute().query.name;
const filter = ref({input: "", set: ""});
const displayDetails = shallowRef(false);
const displayEdit = shallowRef(false);
const toggleReload = shallowRef(false);
const displayCreate = ref(false);
const card = ref<Card>({} as Card);
const cardType = ref<CardType>({} as CardType);

cardType.value = CardType.SIMPLEQA;

const openCard = async (id: string) => {
  displayDetails.value = true;
  card.value = await useCardsService().getCardById(id, props.categoryId);
};

const addCard = async () => {
  card.value = {} as Card;

  // shared fields of all types of card
  card.value.tags = [];
  card.value.title = 'new card';
  card.value.question = 'New card?';

  switch (cardType.value) {
    case CardType.SIMPLEQA:
      card.value.type = CardType.SIMPLEQA;
      card.value.answer = 'New card';
      break;
    case CardType.MULTIPLE_CHOICE:
      card.value.type = CardType.MULTIPLE_CHOICE;
      card.value.correctOptions = [0];
      card.value.options = ['choice 1'];
      break;
    case CardType.SINGLE_CHOICE:
      card.value.type = CardType.SINGLE_CHOICE;
      card.value.options = ['choice 1'];
      card.value.correctOption = 0;
      break;
    default:
      break;
  }
  displayCreate.value = true;
  displayEdit.value = true;
};

const updateCard = async (newCard: Card) => {
  newCard.tags = newCard.tags.filter(tag => !!tag);
  if (displayCreate.value) {
    await useCardsService().createCard(props.categoryId, newCard);
    card.value = {} as Card;
  } else {
    card.value = await useCardsService().putCard(props.categoryId, newCard);
  }
  toggleReload.value = !toggleReload.value;
  toggleEdit();
};

const deleteCard = async (curCard: Card) => {
  await useCardsService().deleteCard(props.categoryId, curCard);
  toggleReload.value = !toggleReload.value;
  displayEdit.value = false;
  displayDetails.value = false;
};

const toggleEdit = () => {
  if (!displayCreate.value) {
    displayDetails.value = !displayDetails.value;
    displayEdit.value = !displayEdit.value;
  } else {
    displayCreate.value = false;
    displayEdit.value = false;
  }

};
</script>
