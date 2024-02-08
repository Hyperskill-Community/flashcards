<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-card color="secondary" class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between">
          <v-card-title v-text="card.title"/>
          <v-card-subtitle class="pa-2 ma-2 d-flex justify-space-between">
            <v-card-text v-text="'Tags: '"/>
            <v-list class="ma-0 pa-0 d-flex flex-row flex-wrap">
              <v-list-item v-for="tag in card.tags" :key="tag"
                           :title="tag"/>
            </v-list>
          </v-card-subtitle>

          <v-card-text>
            {{ card.question }}
            <v-container class="pa-0 mt-2">
              <v-list v-if="card.type !== CardType.SIMPLEQA" class="pa-0 d-flex flex-column flex-wrap">
                <v-list-item v-for="(option, index) in card.options" :key="option"
                             :title="parseOption(index, option, card)"
                             @click="toggleOption(option, card)"
                             :disabled="answerShown"
                             class="cursor-pointer"
                             :class="{'correct': answerShown && isCorrect(option, card),
                                      'error': selected.includes(option) && answerShown && !isCorrect(option, card),
                                      'selected': !answerShown && selected.includes(option)}"/>
              </v-list>
              <v-text-field v-else v-model="providedAnswer"
                            :class="{'correct': answerShown && isCorrect(providedAnswer, card),
                                     'error': answerShown && !isCorrect(providedAnswer, card)}"/>
            </v-container>
          </v-card-text>

          <v-container>
            <v-row class="pa-0 d-flex w-100 justify-space-between align-center">
              <v-btn color="green" :disabled="!selected && !providedAnswer"
                     @click="checkCorrectAnswer(card, selected.length ? selected : [providedAnswer])" variant="text"
                     border>
                <v-icon icon="mdi-check" size="large" start/>
                Check Answer
              </v-btn>
              <v-card-subtitle v-show="answerShown" v-text="showCorrectAnswer(card)"/>
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

defineProps<({
  card: Card,
})>();

const answerShown = ref(false);
const selected = ref([] as string[]);
const providedAnswer = ref('');
const isCorrectAnswer = ref(false);

const parseOption = (index: number, option: string, card: Card): string => {
  return `${String.fromCharCode(65 + index)}. ${option}`;
};

const isCorrect = (option: string, card: Card) => {
  return getCorrectAnswer(card)?.includes(option);
};

const toggleOption = (option: string, card: Card) => {
  const index = selected.value.indexOf(option);
  if (index === -1) {
    if (card.type === CardType.SINGLE_CHOICE && !selected.value.includes(option)) {
      selected.value = [option];
    } else {
      selected.value.push(option);
    }
  } else {
    selected.value.splice(index, 1)
  }
};

const checkCorrectAnswer = (card: Card, answers: string[]) => {
  answerShown.value = !answerShown.value;
  const correctAnswer = getCorrectAnswer(card);
  console.log(correctAnswer);
  isCorrectAnswer.value = answers.every(answer => correctAnswer?.includes(answer));
};

const getCorrectAnswer = (card: Card) => {
  console.log("card type is " , card.type);
  switch (card.type) {
    case CardType.SINGLE_CHOICE:
      return card.options[Number(card.correctOption)];
    case CardType.SIMPLEQA:
      return [card.answer];
    case CardType.MULTIPLE_CHOICE:
      console.log("correct options are here ", card.correctOptions);
      return card.correctOptions.map(i => card.options[Number(i)]).join(", ");
  }
};

const showCorrectAnswer = (card: Card) => {
  const correctAnswer = getCorrectAnswer(card);
  return `Correct answer${card.type === CardType.MULTIPLE_CHOICE ? 's' : ''}: ${correctAnswer}`;
};
</script>

<style scoped lang="scss">
.correct {
  border: 1px solid green;
  border-radius: .25rem;
  background-color: rgb(0, 255, 0, .8);
}

.selected {
  border: 1px solid green;
  border-radius: .25rem;
  background-color: rgb(0, 128, 0, .5);
}

.error {
  border: 1px solid red;
  border-radius: .25rem;
  background-color: rgb(255, 0, 0, .5);
}
</style>
