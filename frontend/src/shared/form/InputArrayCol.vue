<template>
  <v-col :md="md">
    <div class="d-flex flex-wrap justify-sm-space-between ma-n3">
      <v-col v-for="index in model.length + 1" :key="index" :md="fieldMd">
        <v-text-field clearable density="compact" class="mt-n5 mb-n5"
                      @click:clear="shiftDown(index)"
                      :label="`${prompt} #${index}`"
                      :model-value="model[index - 1]"
                      :rules="index - 1 < required ? [(v) => !!v || `${required} ${prompt}s required`] : []"
                      @update:model-value="(val) => model[index - 1] = val"/>
      </v-col>
    </div>
  </v-col>
</template>

<script setup lang="ts">
import {computed} from "vue";

const props = withDefaults(defineProps<({
  modelValue: string[],
  prompt: string,
  md?: number,
  fieldMd?: number,
  required?: number,
})>(), {
  md: 12,
  fieldMd: 6,
  required: 0,
});

const emit = defineEmits<({
  'update:modelValue': [value: string[]],
})>();

const model = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
});

const shiftDown = (index: number) => {
  model.value.splice(index - 1, 1);
};
</script>

