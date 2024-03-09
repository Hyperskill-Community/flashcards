<template>
  <v-col :md="md">
    <div class="d-flex flex-wrap justify-sm-space-between ma-n3">
      <v-checkbox v-for="index in Array.from({ length: groupSize }, (_, ind) => ind)" :key="index"
                  class="mt-n4 mb-n4 ml-n4" color="green"
                  label="correct" :value="index"
                  :model-value="model" @update:model-value="(val) => model = val!"/>
    </div>
  </v-col>
</template>

<script setup lang="ts">

import {computed} from "vue";

const props = withDefaults(defineProps<({
  modelValue: number[],
  groupSize: number,
  md?: number,
})>(), {
  md: 2,
});

const model = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
});

const emit = defineEmits<({
  'update:modelValue': [value: number[]],
})>();
</script>
