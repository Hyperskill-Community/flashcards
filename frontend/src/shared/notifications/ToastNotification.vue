<template>
  <div class="notification">
    <div v-text="notification.header" :class="notification.type" class="pa-3 font-weight-bold"/>
    <div v-html="notification.message" class="pa-3"/>
  </div>
</template>

<script setup lang="ts">
import {PropType} from "vue";
import {Toast, toasts} from "@/shared/composables/toastService";

const props = defineProps({
  notificationKey: {
    type: Number,
    required: true,
  },
  notification: {
    type: Object as PropType<Toast>,
    required: true,
  }
});

setTimeout(() => toasts.value.delete(props.notificationKey), 5000);
</script>

<style scoped lang="scss">
@import "@/assets/colors";

.notification {
  position: relative;
  width: 30rem;
  display: grid;
  align-items: center;
  column-gap: 1rem;
  background-color: $--white;
  border-radius: .5rem;
  overflow: hidden;
  box-shadow: 0 .25rem .5rem $--dark;

  .success {
    background-color: $--green-success;
    color: $--white;
  }

  .error {
    background-color: $--red-error;
    color: $--white;
  }

  .warning {
    background-color: $--yellow-warning;
  }
}
</style>
