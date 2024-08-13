<template>
  <base-card-page>
    <v-card-title class="text-center text-h4">
      {{ mode }} card of type {{ translateType(card.type) }}
    </v-card-title>
    <v-card-subtitle class="text-center">
      Submit Changes with Enter or Save button
    </v-card-subtitle>

    <v-card-text>
      <v-form
        v-model="formValid"
        @submit.prevent="() => !!formValid && emit('update', newCard)"
      >
        <input
          type="submit"
          hidden
        ><!-- Required for the form to submit on enter -->
        <v-row>
          <prompt-input-line
            v-model="newCard.title!"
            prompt="Title"
          />
          <input-array-col
            v-model="newCard.tags"
            prompt="Tag"
          />
          <prompt-input-line
            v-model="newCard.question"
            prompt="Question"
            required
          />
          <!-- Simple Q & A -->
          <prompt-input-line
            v-if="card.type===CardType.SIMPLEQA"
            v-model="newCard.answer!"
            prompt="Answer"
            required
            label-class="mt-n2"
            input-class="mt-n4 mb-n4"
          />
          <!-- Quiz Cards -->
          <input-array-col
            v-if="card.type!==CardType.SIMPLEQA"
            v-model="newCard.options!"
            prompt="Option"
            :required="2"
            :md="10"
            :field-md="12"
          />
          <radio-button-col
            v-if="card.type===CardType.SINGLE_CHOICE"
            v-model="newCard.correctOption!"
            :group-size="newCard.options!.length"
          />
          <check-box-col
            v-if="card.type===CardType.MULTIPLE_CHOICE"
            v-model="newCard.correctOptions!"
            :group-size="newCard.options!.length"
          />
        </v-row>
      </v-form>
    </v-card-text>

    <v-card-actions class="pa-2 ma-0">
      <v-spacer />
      <save-mdi-button
        :disabled="!formValid"
        :click-handler="emitAction"
      />
      <cancel-mdi-button
        tooltip-text="Reset content"
        :click-handler="resetNewCard"
      />
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <back-mdi-button
        tooltip-text="Back to Category"
        :click-handler="() => emit('close', true)"
      />
    </v-card-actions>
  </base-card-page>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {Card, CardType, clone, translateType} from "@/feature/cards/model/card";
import BackMdiButton from "@/shared/buttons/BackMdiButton.vue";
import SaveMdiButton from "@/shared/buttons/SaveMdiButton.vue";
import CancelMdiButton from "@/shared/buttons/CancelMdiButton.vue";
import PromptInputLine from "@/shared/form/PromptInputLine.vue";
import InputArrayCol from "@/shared/form/InputArrayCol.vue";
import RadioButtonCol from "@/feature/cards/components/RadioButtonCol.vue";
import CheckBoxCol from "@/feature/cards/components/CheckBoxCol.vue";
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";

const props = defineProps<({
  card: Card,
  mode: 'Edit' | 'Add',
})>();

const emit = defineEmits<({
  'close': [val: boolean],
  'post': [val: Card],
  'update': [val: Card],
})>();

const newCard = ref<Card>(clone(props.card));
const formValid = ref(false);

const emitAction = () => props.mode === 'Edit'
  ? emit('update', newCard.value)
  : emit('post', newCard.value);

const resetNewCard = () => newCard.value = clone(props.card);
</script>
