<template>
  <v-infinite-scroll
    :items="items"
    :onLoad="fetchCardsPage"
    :max-height="'60vh'"
    empty-text="No more cards"
  >
    <template
      v-for="(item, index) in items"
      :key="item"
    >
      <div
        :class="['cursor-pointer', 'pa-3', 'd-flex', 'justify-space-between', 'align-center',
                 'v-col-sm-12', {'bg-white': index % 2 === 0}]"
        @click="() => emit('openCard', item.id)"
      >
        <v-avatar
          color="primary"
          size="large"
          class="mr-5"
          v-text="`${index + 1}`"
        />
        {{ item.question }}
        <v-spacer />
        <v-avatar
          color="primary"
          size="large"
          class="mr-10"
          v-text="item.type.toUpperCase()"
        />
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
  reload: boolean,
})>();

const emit = defineEmits<({
  'openCard': [val: string],
})>();

const items = ref<CardItem[]>([]);
const pagePointer = ref({current: 0, isLast: false});
const doneCallback = ref<(status: 'empty' | 'ok') => void>(() => {});

watch(() => props.filter, async () => await loadFiltered());
watch(() => props.reload, async () => await loadFiltered());

const fetchCardsPage = async ({done}: { done: (status: 'empty' | 'ok') => void }) => {
  doneCallback.value = done;
  if (pagePointer.value.isLast) {
    done('empty');
    return;
  }
  const cardResponse = await useCardsService().getCards(props.categoryId, props.filter, pagePointer.value.current);
  pagePointer.value.current = cardResponse.currentPage + 1;
  pagePointer.value.isLast = cardResponse.isLast;
  items.value = [...items.value, ...cardResponse.cards];
  done('ok');
};

const loadFiltered = async () => {
  pagePointer.value = {current: 0, isLast: false}; // reset the page pointer
  items.value = []; // clear the items array
  doneCallback.value('ok'); // reset internal state of VInfiniteScroller (see https://github.com/vuetifyjs/vuetify/issues/19935)
};
</script>
