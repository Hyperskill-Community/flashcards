<template>
  <v-card>
    <v-card-title class="d-flex align-center">
      <h4>{{ name }}</h4>
    </v-card-title>

    <v-card-text>
      {{ category.description }}
    </v-card-text>

    <div class="px-4">
      <v-switch
        :model-value="expanded"
        :label="`Show details`"
        :color="`${expanded ? '#43A047' : '#EEEEEE'}`"
        density="compact"
        inset
        @click="() => toggleExpand(expanded)"
      ></v-switch>
    </div>

    <v-divider></v-divider>

    <v-expand-transition>
      <div v-if="expanded">
        <v-list density="compact" :lines="false">
          <v-list-item :title="`🔥 Your access: ${getAccess(category)}`"></v-list-item>
          <v-list-item :title="`🍔 #Cards in Category: ${category.numberOfCards}`"></v-list-item>
          <v-list-item :title="`🧲 Id: ${category.id}`"></v-list-item>
        </v-list>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script setup lang="ts">
import {Category, getAccess} from "@/feature/category/model/category";

defineProps<({
  name: string
  category: Category
  expanded: boolean
})>();

const emit = defineEmits<{
  'update:expanded': [val: boolean]
}>();

const toggleExpand = (expanded: boolean) => {
  emit('update:expanded', !expanded);
}
</script>

<style scoped lang="scss">

</style>
