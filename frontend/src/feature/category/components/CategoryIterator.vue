<template>
  <v-data-iterator
    :items="props.categories"
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
                :model-value="isExpanded(item as any)"
                :label="`Show details`"
                :color="`${isExpanded(item as any) ? '#43A047' : '#EEEEEE'}`"
                density="compact"
                inset
                @click="() => toggleExpand(item as any)"
              ></v-switch>
            </div>

            <v-divider></v-divider>

            <v-expand-transition>
              <div v-if="isExpanded(item as any)">
                <v-list density="compact" :lines="false">
                  <v-list-item :title="`🔥 Your access: ${getAccess(item.raw)}`"></v-list-item>
                  <v-list-item :title="`🍔 #Cards in Category: ${item.raw.numberOfCards}`"></v-list-item>
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
import {Category, getAccess} from "@/feature/category/model/category";

const props = defineProps({
  categories: Array as () => Category[]
})

</script>
