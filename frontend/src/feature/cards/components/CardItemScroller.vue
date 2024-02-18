<template>
  <v-infinite-scroll :items="items"
                     :onLoad="fetchCardsPage"
                     :max-height="'60vh'"
                     empty-text="No more cards">
    <template v-for="(item, index) in items" :key="item">
      <div
        :class="['pa-2', 'd-flex', 'justify-space-between', 'align-center', {'bg-grey-lighten-2': index % 2 === 0}]">
        <v-avatar v-text="`${index + 1}`" color="primary" size="large" class="mr-5"/>
        {{ item.title }}
        <v-spacer/>
        <v-avatar v-text="item.type.toUpperCase()" color="primary" size="large" class="mr-10"/>
        <open-mdi-button :clickHandler="() => openCard(item.id)" tooltip-text="Open Card Details"/>
      </div>
    </template>
  </v-infinite-scroll>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {useRouter} from "vue-router";
import {CardItem} from "@/feature/cards/model/card";
import OpenMdiButton from "@/shared/components/OpenMdiButton.vue";
import useCardsService from "@/feature/cards/composables/useCardsService";

const props = defineProps<({
  categoryId: string,
  titleFilter: string,
})>();

const items = ref([] as CardItem[]);
const router = useRouter();
const pagePointer = ref({current: 0, isLast: false});

watch(() => props.titleFilter, async (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pagePointer.value = {current: 0, isLast: false}; // reset the page pointer
    items.value = []; // clear the items array
    await fetchCardsPage({done: () => console.log("Manual reload after filter change")});
  }
});

const openCard = (id: string) => router.push(`/card/${props.categoryId}/${id}`);

const fetchCardsPage = async ({done}: { done: Function }) => {
  if (pagePointer.value.isLast) {
    done('empty');
    return;
  }
  const cardResponse = await useCardsService().getCards(props.categoryId, props.titleFilter, pagePointer.value.current);
  pagePointer.value.current = cardResponse.currentPage + 1;
  pagePointer.value.isLast = cardResponse.isLast;
  for (const cardItem of cardResponse.cards) {
    if (items.value.find((item) => item.id === cardItem.id)) {
      continue; // necessary, since method is called manually and automatically after filter change
    }
    items.value.push(cardItem);
  }
  done('ok');
};
</script>
