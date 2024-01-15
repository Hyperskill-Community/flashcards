<template>
  <v-data-iterator
    :items="categories"
    item-value="name"
  >
    <template v-slot:default="{ items, isExpanded, toggleExpand }">
      <v-row>
        <v-col
          v-for="item in items"
          :key="item.raw.name"
          cols="12"
          sm="12"
          md="6"
        >
          <v-card>
            <v-card-title class="d-flex align-center">
              <v-icon
                :color="item.raw.color"
                :icon="item.raw.icon"
                start
                size="18"
              ></v-icon>

              <h4>{{ item.raw.name }}</h4>
            </v-card-title>

            <v-card-text>
              {{ item.raw.description }}
            </v-card-text>

            <div class="px-4">
              <v-switch
                :model-value="isExpanded(item)"
                :label="`Show details`"
                :color="`${isExpanded(item) ? '#43A047' : '#EEEEEE'}`"
                density="compact"
                inset
                @click="() => toggleExpand(item)"
              ></v-switch>
            </div>

            <v-divider></v-divider>

            <v-expand-transition>
              <div v-if="isExpanded(item)">
                <v-list density="compact" :lines="false">
                  <v-list-item :title="`🔥 Your access: ${item.raw.access}`" active></v-list-item>
                  <v-list-item :title="`🍔 #Cards in Category: ${item.raw.numberOfCards || 0}`"></v-list-item>
                  <v-list-item :title="`🧲 Id: ${item.raw.id}`"></v-list-item>
                </v-list>
              </div>
            </v-expand-transition>
          </v-card>
        </v-col>
      </v-row>
    </template>
  </v-data-iterator>
</template>

<script setup lang="ts">
import {Category} from "@/feature/category/model/category";
import {ref} from "vue";

const props = defineProps({
  categories: Array as () => Category[]
})
const categories = ref([
  {
    id: 'abcdef',
    name: 'Example',
    access: 'rwd',
    numberOfCards: 10,
    description: 'This is an example category',
  },
  {
    id: 'abcdf',
    name: 'Mathe',
    access: 'rw',
    numberOfCards: 125,
    description: 'These are my math flashcards',
  },
  {
    id: 'abcde',
    name: 'Java',
    access: 'rd',
    description: 'These are my Java flashcards',
  },
] as Category[]);

</script>
