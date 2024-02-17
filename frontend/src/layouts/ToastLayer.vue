<template>
  <div class="layer">
    <transition-group name="slide">
      <toast-notification v-for="[key, toast] in toasts.entries()" :key="key"
                          :notification="toast" :notification-key="key"/>
    </transition-group>
  </div>
</template>

<script setup lang="ts">
import ToastNotification from "@/shared/notifications/ToastNotification.vue";
import {toasts} from "@/shared/composables/toastService";
</script>

<style scoped lang="scss">
@import "@/assets/colors";

.layer {
  position: absolute;
  top: 6rem;
  right: 2rem;
  z-index: 1000;
  display: flex;
  flex-direction: column-reverse;
  gap: 1.25rem;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(150%);
}

.slide-enter-active,
.slide-leave-active {
  transition: .5s;
}

</style>
