<template>
  <v-tooltip :disabled="!tooltipText" :text="tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-btn variant="text"
             type="submit"
             icon="mdi-send-circle-outline"
             class = "mb-5 fill-height"
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
  // no tooltip by default
  tooltipText: {
    type: String,
    default: "Submit"
  }
});
</script>
