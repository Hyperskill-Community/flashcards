<template>
  <v-col :md="md">
    <div class="d-flex flex-wrap justify-sm-space-between ma-n3">
      <v-radio-group v-for="index in Array.from({ length: groupSize }, (_, ind) => ind)" :key="index"
                     :model-value="model"
                     @update:model-value="val => model = val!">
        <v-radio :value="index" class="ml-n4 mt-n2 mb-n2" label="correct" color="green"/>
      </v-radio-group>
    </div>
  </v-col>
</template>

<script setup lang="ts">
import {computed} from "vue";

const props = withDefaults(defineProps<({
  modelValue: number,
  groupSize: number,
  md?: number,
})>(), {
  md: 2,
});

const emit = defineEmits<({
  'update:modelValue': [value: number],
})>();

const model = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
});
</script>
