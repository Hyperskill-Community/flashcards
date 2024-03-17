<template>
  <base-card-page>
    <v-card-title v-text="card.title" class="align-self-center"/>
    <v-card-subtitle class="pa-2 ma-2 d-flex justify-space-between">
      <v-card-text v-text="'Tags: '"/>
      <v-list class="ma-0 pa-2 d-flex flex-row flex-wrap">
        <v-list-item v-for="tag in card.tags" :key="tag" :title="tag"/>
      </v-list>
    </v-card-subtitle>

    <v-card-text>
      <h4 class="pa-2 ma-2">{{ card.question }}</h4>
      <v-container class="pa-2 mt-2">
        <v-text-field v-if="card.type === CardType.SIMPLEQA" v-model="providedAnswer"
                      density="compact" label="Your Answer"
                      :class="{'correct': answerShown && isCorrect(providedAnswer),
                                     'error': answerShown && !isCorrect(providedAnswer)}"/>
        <v-list v-else class="pa-0 d-flex flex-column flex-wrap">
          <v-list-item v-for="(option, index) in card.options" :key="option"
                       :title="parseOption(index, option)"
                       @click="toggleOption(option)"
                       :disabled="answerShown"
                       class="cursor-pointer"
                       :class="{'correct': answerShown && isCorrect(option),
                                      'error': selected.includes(option) && answerShown && !isCorrect(option),
                                      'selected': !answerShown && selected.includes(option)}"/>
        </v-list>
      </v-container>
    </v-card-text>

    <v-container>
      <v-row class="pa-3 d-flex justify-space-around">
        <v-btn @click="highlightCorrectAnswers()" :disabled="!selected && !providedAnswer"
               prepend-icon="mdi-check-circle" color="green" variant="outlined">
          Check Answer
        </v-btn>
      </v-row>
    </v-container>

    <v-card-actions class="pa-2 ma-0">
      <v-spacer/>
      <edit-mdi-button tooltip-text="Edit Card"
                       :click-handler="() => emit('edit', true)"/>
      <delete-mdi-button :click-handler="() => {}" tooltip-text="Delete Card - not implemented"/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <back-mdi-button tooltip-text="Back to Category"
                       :click-handler="() => emit('close', true)"/>
    </v-card-actions>
  </base-card-page>
</template>

<script setup lang="ts">
import EditMdiButton from "@/shared/buttons/EditMdiButton.vue";
import DeleteMdiButton from "@/shared/buttons/DeleteMdiButton.vue";
import {Card, CardType} from "@/feature/cards/model/card";
import BackMdiButton from "@/shared/buttons/BackMdiButton.vue";
import {ref, shallowRef} from "vue";
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";

const props = defineProps<({
  card: Card,
})>();

const emit = defineEmits<({
  'close': [val: boolean],
  'edit': [val: boolean],
})>();

const answerShown = shallowRef(false);
const selected = ref([] as string[]);
const providedAnswer = shallowRef('');

const parseOption = (index: number, option: string) => `${String.fromCharCode(65 + index)}. ${option}`;
const isCorrect = (option: string) => getCorrectAnswer()?.includes(option);
const highlightCorrectAnswers = () => answerShown.value = !answerShown.value;

const toggleOption = (option: string) => {
  const index = selected.value.indexOf(option);
  if (index === -1) {
    if (props.card.type === CardType.SINGLE_CHOICE && !selected.value.includes(option)) {
      selected.value = [option];
    } else {
      selected.value.push(option);
    }
  } else {
    selected.value.splice(index, 1);
  }
};

const getCorrectAnswer = () => {
  const card = props.card;
  switch (card.type) {
    case CardType.SINGLE_CHOICE:
      return card.options![Number(card.correctOption)];
    case CardType.SIMPLEQA:
      return [card.answer];
    case CardType.MULTIPLE_CHOICE:
      return card.correctOptions!.map(i => card.options![Number(i)]).join(", ");
  }
};
</script>

<style scoped lang="scss">
@import "@/assets/colors";

.correct {
  border: 1px solid $--green-correct-answer;
  border-radius: .25rem;
  background-color: $--green-correct-answer-background;
}

.selected {
  border: 1px solid $--green-correct-answer;
  border-radius: .25rem;
  background-color: $--green-selected-answer-background;
}

.error {
  border: 1px solid $--red-wrong-answer;
  border-radius: .25rem;
  background-color: $--red-wrong-answer-background;
}
</style>
