<template>
  <v-tooltip :disabled="!tooltipText" :text="tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-btn variant="text"
             icon="mdi-close-outline"
             class = "add-button ms-n6"
             v-bind="props"
             :disabled="disabled"
             color="deep-orange"
             @click="clickHandler"
             size="x-large"/>
    </template>
  </v-tooltip>
</template>

<script setup lang="ts">
withDefaults(defineProps<({
  clickHandler: Function,
  disabled?: boolean,
  tooltipText?: string,
})>(), {
  disabled: false,
  tooltipText: ''
});
</script>
