<template>
  <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" fill-height color="containerBackground">
    <v-card-title v-text="'User Registration'" class="text-center text-h4"/>
    <v-card-text>
      <v-row>
        <v-col cols="12" sm="6" md="4"/>
        <v-form cols="12" sm="6" md="4" class="align-content-center">
          <input type="submit" hidden /><!-- Required for the form to submit on enter -->
          <v-file-input v-model="filePath" label="Export to ..." prepend-icon="mdi-camera"/>
          <v-btn color="black" border @click="exportData" variant="text">
            Export your data
          </v-btn>
        </v-form>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import {ref} from "vue";

const filePath = ref<File[]>([]);

const exportData = () => {
  const data = new FormData();
  data.append('file', filePath.value[0]);
  fetch('http://localhost:5000/export', {
    method: 'POST',
    body: data,
  });
};
</script>
