<template>
  <v-tooltip
    :disabled="!tooltipText"
    :text="tooltipText"
    location="top"
  >
    <template #activator="{ props }">
      <v-btn
        variant="text"
        icon="mdi-content-save-outline"
        class="add-button ms-n6"
        v-bind="props"
        :disabled="disabled"
        color="#0b0be5"
        size="x-large"
        @click="clickHandler"
      />
    </template>
  </v-tooltip>
</template>

<script setup lang="ts">
withDefaults(defineProps<({
  clickHandler: () => void,
  disabled?: boolean,
  tooltipText?: string,
})>(), {
  disabled: false,
  tooltipText: 'Save'
});
</script>
