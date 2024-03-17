<template>
  <v-textarea clearable rows="1" auto-grow dense
                @click:clear="() => updateValue('')"
                :model-value="modelValue"
                :label="prompt"
                @update:modelValue="updateValue"
                :rules="required ? [v => !!v || 'must not be empty'] : []"
                :class="`v-col-sm-${inputWidth} ${inputClass}`" density="compact"/>
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
});

const emit = defineEmits<({
  'update:modelValue': [value: string],
})>();

const updateValue = (value: string) => emit('update:modelValue', value);

</script>
