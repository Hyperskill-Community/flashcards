<template>
  <v-app>
    <side-navigation/>
    <v-main>
      <router-view/>
      <toast-layer/>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import ToastLayer from "@/layouts/ToastLayer.vue";
import SideNavigation from "@/layouts/SideNavigation.vue";
</script>
