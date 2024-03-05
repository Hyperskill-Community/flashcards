<template>
  <v-row justify="center">
    <v-col md="9" lg="9">
      <v-card color="secondary" class="pa-2 ma-2 d-flex flex-column">
        <v-card-title class="text-center text-h4 pb-5">
          Edit card of type QNA
        </v-card-title>

        <v-card-text>
          <v-row class="align-content-center">
            <v-col md="2" class="text-h6">Title:</v-col>
            <v-text-field clearable class="v-col-sm-10" density="compact" v-model="newCard.title"/>
            <v-col md="2" class="text-h6">Tags:</v-col>
            <v-col md="10">
              <div class="d-flex flex-wrap justify-sm-space-between ma-n3">
                <v-col v-for="index in 4" :key="index" md="6">
                  <v-text-field clearable density="compact"
                                :disabled="indexEnabled(index)"
                                v-model="newCard.tags[index - 1]"/>
                </v-col>
              </div>
            </v-col>
            <v-col sm="2" class="text-h6">Question:</v-col>
            <v-text-field clearable class="v-col-sm-10" density="compact" v-model="newCard.question"/>
            <v-col sm="2" class="text-h6">Answer:</v-col>
            <v-text-field clearable class="v-col-sm-10" density="compact" v-model="newCard.answer"/>
          </v-row>
        </v-card-text>

        <v-card-actions class="pa-2 ma-0">
          <v-spacer/>
          <save-mdi-button :click-handler="() => emit('update', newCard)"/>
          <cancel-mdi-button tooltip-text="Reset content"
                             :click-handler="resetNewCard"/>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <back-mdi-button tooltip-text="Back to Category"
                           :click-handler="() => emit('close', true)"/>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script setup lang="ts">
import {Card, clone} from "@/feature/cards/model/card";
import BackMdiButton from "@/shared/components/BackMdiButton.vue";
import {ref} from "vue";
import SaveMdiButton from "@/shared/components/SaveMdiButton.vue";
import CancelMdiButton from "@/shared/components/CancelMdiButton.vue";

const props = defineProps<({
  card: Card,
})>();

const emit = defineEmits<({
  'close': [val: boolean],
  'update': [val: Card],
})>();

const newCard = ref<Card>(clone(props.card));

const resetNewCard = () => {
  newCard.value = clone(props.card);
};

const indexEnabled = (index: number) => {
  return index > 1 && newCard.value.tags.slice(index - 2, 4).every(tag => !tag);
};

</script>
