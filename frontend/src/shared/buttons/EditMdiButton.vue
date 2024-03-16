<template>
  <v-tooltip :disabled="!tooltipText" :text="tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-btn variant="text"
             icon="mdi-square-edit-outline"
             class = "edit-button ms-n6"
             v-bind="props"
             :disabled="disabled"
             color="#0b0be5"
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
