<template>
  <v-tooltip :disabled="!tooltipText" :text="tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-btn variant="text"
             icon="mdi-content-save-outline"
             class = "add-button ms-n6"
             v-bind="props"
             :disabled="disabled"
             @click="clickHandler"
             size="x-large"/>
    </template>
  </v-tooltip>
</template>

<script setup lang="ts">
defineProps({
  clickHandler: Function,
  disabled:  {
    type: Boolean,
    default: false
  },
  tooltipText: {
    type: String,
    default: "Save"
  }
});
</script>
