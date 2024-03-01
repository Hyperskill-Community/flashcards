<template>
  <v-container v-if="totalPages > 1" class="d-flex align-center justify-center pa-4">
    <v-btn :disabled="page === 1" icon="mdi-chevron-left" density="comfortable" variant="tonal" rounded
           @click="() => emit('update:page', page - 1)"
    ></v-btn>
    <div class="mx-2 text-caption">
      Page {{ page }} of {{ totalPages }}
    </div>
    <v-btn :disabled="page >= totalPages" icon="mdi-chevron-right" density="comfortable" variant="tonal" rounded
           @click="() => emit('update:page', page + 1)"
    ></v-btn>
  </v-container>
</template>

<script setup lang="ts">
defineProps<({
  totalPages: number,
  page: number
})>();

const emit = defineEmits<{
  'update:page': [val: number],
}>();
</script>
