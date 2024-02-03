<template>
  <v-infinite-scroll :height="'60vh'" :items="items" :onLoad="load">
    <template v-for="(item, index) in items" :key="item">
      <div :class="['pa-2', 'd-flex', 'justify-space-between', 'align-center', index % 2 === 0 ? 'bg-grey-lighten-2' : '']">
        <v-avatar color="primary" size="large" class="mr-5">{{index + 1}}</v-avatar>
        {{ item.title }}
        <v-spacer></v-spacer>
        <v-avatar color="primary" size="large" class="mr-10">{{item.type.toUpperCase()}}</v-avatar>
        <open-mdi-button tooltip-text="Open Card Details"
                         :clickHandler="() => openCard(item.id)"/>
      </div>
    </template>
  </v-infinite-scroll>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {CardItem, CardType} from "@/feature/cards/model/card";
import OpenMdiButton from "@/shared/components/OpenMdiButton.vue";
import {useRouter} from "vue-router";

const props = defineProps<({
  categoryId: String,
  filter: String,
})>();
const items = ref([] as CardItem[]);
const router = useRouter();
const fetchCardsForCategory = async () => {
  items.value = [];
}
fetchCardsForCategory();
const openCard = (id: string) => {
  console.log("Opening card with id: " + id);
  router.push(`/display/${id}`);
}

// ------------------- TEST DATA -------------------
// ---uncomment if on npm run mode - and comment out the fetchCategories() call
const load = async ({done} : {done: Function}) => {
  console.log(`Loading cards ... Now we have ${items.value.length} items.`);
  items.value.push({
    id: "123",
    title: "A math question",
    type: CardType.SIMPLEQA,
  });
  items.value.push({
    id: "456",
    title: "A Java question",
    type: CardType.SINGLE_CHOICE,
  });
  items.value.push({
    id: "456",
    title: "Another Java question",
    type: CardType.SINGLE_CHOICE,
  });
  items.value.push({
    id: "456",
    title: "Another Math question",
    type: CardType.SINGLE_CHOICE,
  });
  items.value.push({
    id: "456",
    title: "Another Python question",
    type: CardType.SINGLE_CHOICE,
  });
  items.value.push({
    id: "456",
    title: "A Python question",
    type: CardType.SINGLE_CHOICE,
  });
  done('ok');
}
load({done: console.log});
</script>
