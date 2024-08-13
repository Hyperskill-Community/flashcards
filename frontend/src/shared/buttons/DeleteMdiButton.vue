<template>
  <v-tooltip
    :disabled="!tooltipText"
    :text="tooltipText"
    location="top"
  >
    <template #activator="{ props }">
      <v-btn
        variant="text"
        icon="mdi-trash-can-outline"
        class="delete-button ms-n6"
        v-bind="props"
        :disabled="disabled"
        color="deep-orange"
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
  tooltipText: ''
});
</script>
