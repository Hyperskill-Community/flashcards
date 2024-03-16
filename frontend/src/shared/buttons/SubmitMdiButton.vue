<template>
  <v-tooltip :disabled="!tooltipText" :text="tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-btn variant="text"
             type="submit"
             icon="mdi-send-circle-outline"
             class = "submit-button mb-5 fill-height"
             v-bind="props"
             color="#0b0be5"
             :disabled="disabled"
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
  tooltipText: 'Submit'
});
</script>
