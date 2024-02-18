<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-card color="secondary" class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between">
          <v-card-title v-text="card.title"/>
          <v-card-subtitle class="pa-2 ma-2 d-flex justify-space-between">
            <v-card-text v-text="'Tags: '"/>
            <v-list class="ma-0 pa-0 d-flex flex-row flex-wrap">
              <v-list-item v-for="tag in card.tags" :key="tag" :title="tag"/>
            </v-list>
          </v-card-subtitle>

          <v-card-text>
            {{ card.question }}
            <v-container class="pa-0 mt-2">
              <v-list v-if="card.type !== CardType.SIMPLEQA" class="pa-0 d-flex flex-column flex-wrap">
                <v-list-item v-for="(option, index) in card.options" :key="option"
                             :title="parseOption(index, option)"
                             @click="toggleOption(option)"
                             :disabled="answerShown"
                             class="cursor-pointer"
                             :class="{'correct': answerShown && isCorrect(option),
                                      'error': selected.includes(option) && answerShown && !isCorrect(option),
                                      'selected': !answerShown && selected.includes(option)}"/>
              </v-list>
              <v-text-field v-else v-model="providedAnswer"
                            :class="{'correct': answerShown && isCorrect(providedAnswer),
                                     'error': answerShown && !isCorrect(providedAnswer)}"/>
            </v-container>
          </v-card-text>

          <v-container>
            <v-row class="pa-0 d-flex w-100 justify-space-between align-center">
              <v-btn @click="highlightCorrectAnswers()" :disabled="!selected && !providedAnswer"
                     prepend-icon="mdi-check-circle" color="green" variant="text">
                Check Answer
              </v-btn>
              <v-card-subtitle v-show="answerShown" v-text="showCorrectAnswer()"/>
            </v-row>
          </v-container>

          <v-container>
            <v-card-actions class="pa-0 ma-0">
              <edit-mdi-button tooltip-text="Edit Card - not implemented"/>
              <delete-mdi-button tooltip-text="Delete Card - not implemented"/>
              <v-spacer/>
              <back-mdi-button tooltip-text="Back to Category"
                               :click-handler="$router.back"/>
            </v-card-actions>
          </v-container>
        </v-card>
      </v-col>
    </v-row>

  </v-container>
</template>

<script setup lang="ts">
import EditMdiButton from "@/shared/components/EditMdiButton.vue";
import DeleteMdiButton from "@/shared/components/DeleteMdiButton.vue";
import {Card, CardType} from "@/feature/cards/model/card";
import BackMdiButton from "@/shared/components/BackMdiButton.vue";
import {ref} from "vue";

const props = defineProps<({
  card: Card,
})>();

const answerShown = ref(false);
const selected = ref([] as string[]);
const providedAnswer = ref('');

const parseOption = (index: number, option: string) => `${String.fromCharCode(65 + index)}. ${option}`;
const isCorrect = (option: string) => getCorrectAnswer()?.includes(option);
const highlightCorrectAnswers = () => answerShown.value = !answerShown.value;
const showCorrectAnswer = () => {
  return `Correct answer${props.card.type === CardType.MULTIPLE_CHOICE ? 's' : ''}: ${getCorrectAnswer()}`;
};

const toggleOption = (option: string) => {
  const index = selected.value.indexOf(option);
  if (index === -1) {
    if (props.card.type === CardType.SINGLE_CHOICE && !selected.value.includes(option)) {
      selected.value = [option];
    } else {
      selected.value.push(option);
    }
  } else {
    selected.value.splice(index, 1)
  }
};

const getCorrectAnswer = () => {
  const card = props.card;
  switch (card.type) {
    case CardType.SINGLE_CHOICE:
      return card.options[Number(card.correctOption)];
    case CardType.SIMPLEQA:
      return [card.answer];
    case CardType.MULTIPLE_CHOICE:
      return card.correctOptions.map(i => card.options[Number(i)]).join(", ");
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
