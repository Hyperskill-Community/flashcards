<template>
  <v-tooltip
    :disabled="!tooltipText"
    :text="tooltipText"
    location="top"
  >
    <template #activator="{ props }">
      <v-btn
        variant="text"
        icon="mdi-open-in-app"
        class="open-button ms-n6"
        v-bind="props"
        :disabled="disabled"
        color="#0b0b45"
        size="x-large"
        @click="clickHandler"
      />
    </template>
  </v-tooltip>
</template>

<script setup lang="ts">
withDefaults(defineProps<({
  // eslint-disable-next-line @typescript-eslint/no-unsafe-function-type
  clickHandler: Function,
  disabled?: boolean,
  tooltipText?: string,
})>(), {
  disabled: false,
  tooltipText: ''
});
</script>
