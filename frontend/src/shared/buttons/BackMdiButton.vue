<template>
  <v-tooltip
    :disabled="!tooltipText"
    :text="tooltipText"
    location="top"
  >
    <template #activator="{ props }">
      <v-btn
        variant="elevated"
        icon="mdi-arrow-left"
        class="back-button ms-n6"
        v-bind="props"
        :disabled="disabled"
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
