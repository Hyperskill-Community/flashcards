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
                :label="`${isExpanded(item) ? 'Hide' : 'Show'} details`"
                density="compact"
                inset
                @click="() => toggleExpand(item)"
              ></v-switch>
            </div>

            <v-divider></v-divider>

            <v-expand-transition>
              <div v-if="isExpanded(item)">
                <v-list density="compact" :lines="false">
                  <v-list-item :title="`🔥 Calories: ${item.raw.calories}`" active></v-list-item>
                  <v-list-item :title="`🍔 Fat: ${item.raw.fat}`"></v-list-item>
                  <v-list-item :title="`🍞 Carbs: ${item.raw.carbs}`"></v-list-item>
                  <v-list-item :title="`🍗 Protein: ${item.raw.protein}`"></v-list-item>
                  <v-list-item :title="`🧂 Sodium: ${item.raw.sodium}`"></v-list-item>
                  <v-list-item :title="`🦴 Calcium: ${item.raw.calcium}`"></v-list-item>
                  <v-list-item :title="`🧲 Iron: ${item.raw.iron}`"></v-list-item>
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

// const props = defineProps({
//   categories: Array as () => Category[]
// })
const categories = ref([
  {
    id: 'abcdef',
    name: 'Example',
    access: 'rwd',
    description: 'This is an example category',
  },
  {
    id: 'abcdf',
    name: 'Mathe',
    access: 'rw',
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
