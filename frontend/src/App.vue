<template>
  <main-layout/>
</template>

<script setup lang="ts">
import MainLayout from "@/layouts/MainLayout.vue";
import {useRouter} from "vue-router";

useRouter().push("/welcome");

</script>
