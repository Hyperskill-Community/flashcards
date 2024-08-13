<template>
  <v-textarea
    clearable
    rows="1"
    auto-grow
    dense
    :model-value="modelValue"
    :label="prompt"
    :rules="required ? [v => !!v || 'must not be empty'] : []"
    :class="`v-col-sm-${inputWidth} ${inputClass}`"
    density="compact"
    @click:clear="() => updateValue('')"
    @update:model-value="updateValue"
  />
</template>

<script setup lang="ts">
withDefaults(defineProps<({
  required?: boolean,
  modelValue: string,
  prompt: string,
  inputWidth?: number,
  inputClass?: string,
})>(), {
  inputWidth: 12,
  inputClass: '',
});

const emit = defineEmits<({
  'update:modelValue': [value: string],
})>();

const updateValue = (value: string) => emit('update:modelValue', value);

</script>
