<template>
  <v-infinite-scroll :items="items"
                     :onLoad="fetchCardsPage"
                     :max-height="'60vh'"
                     empty-text="No more cards">
    <template v-for="(item, index) in items" :key="item">
      <div
        :class="['cursor-pointer', 'pa-3', 'd-flex', 'justify-space-between', 'align-center',
            'v-col-sm-12', {'bg-grey-lighten-2': index % 2 === 0}]"
        @click="() => emit('openCard', item.id)">
        <v-avatar v-text="`${index + 1}`" color="primary" size="large" class="mr-5"/>
        {{ item.question }}
        <v-spacer/>
        <v-avatar v-text="item.type.toUpperCase()" color="primary" size="large" class="mr-10"/>
      </div>
    </template>
  </v-infinite-scroll>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {CardItem} from "@/feature/cards/model/card";
import useCardsService from "@/feature/cards/composables/useCardsService";

const props = defineProps<({
  categoryId: string,
  filter: string,
})>();

const emit = defineEmits<({
  'openCard': [val: string],
})>();

const items = ref<CardItem[]>([]);
const pagePointer = ref({current: 0, isLast: false});

watch(() => props.filter, async () => await loadFiltered());

const fetchCardsPage = async ({done}: { done: Function }) => {
  if (pagePointer.value.isLast) {
    done('empty');
    return;
  }
  const cardResponse = await useCardsService().getCards(props.categoryId, props.filter, pagePointer.value.current);
  pagePointer.value.current = cardResponse.currentPage + 1;
  pagePointer.value.isLast = cardResponse.isLast;
  const newItems = cardResponse.cards.filter(cardItem => !items.value.some(item => item.id === cardItem.id));
  items.value = [...items.value, ...newItems];
  done('ok');
};

const loadFiltered = async () => {
  pagePointer.value = {current: 0, isLast: false}; // reset the page pointer
  items.value = []; // clear the items array
  await fetchCardsPage({done: () => {}}); // trigger new load with filter
};
</script>
