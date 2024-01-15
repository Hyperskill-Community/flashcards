<template>
  <main-layout/>
</template>

<script setup lang="ts">
import MainLayout from "@/layouts/MainLayout.vue";
import {provide} from "vue";

provide("apiBaseUrl", "http://127.0.0.1:8080/api/");
</script>
