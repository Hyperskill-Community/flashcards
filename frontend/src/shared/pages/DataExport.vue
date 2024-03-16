<template>
  <v-row justify="center">
  <v-card fill-height color="secondary" md="9" class="mt-10">
    <v-card-title v-text="'Export your data'" class="text-center text-h4"/>
    <v-card-subtitle v-text="'Here you can export your flashcards data to a JSON file'" class="text-center"/>
    <v-card-item class="text-center">
      <v-alert class="text-deep-orange-darken-3">Note: For large data sets, the export process may take a while.
        You will find the Json File in Browser downloads.
      </v-alert>
    </v-card-item>
    <v-card-item>
      <v-img src="@/assets/folder.jpg" aspect-ratio="2.25" class="mx-auto"/>
    <v-label class="float-right text-sm-subtitle-2 text-grey">Photo by Maksym Kaharlytskyi on Unsplash</v-label>
    </v-card-item>
    <v-card-text class="mt-10">
      <v-row justify="center">

        <v-form cols="12" sm="6" md="4" class="align-content-center">
          <input type="submit" hidden/><!-- Required for the form to submit on enter -->
          <v-btn color="black" border @click="exportData" variant="text">
            Start Export
          </v-btn>
        </v-form>
      </v-row>
    </v-card-text>
    <v-card-item v-if="exportUrl" class="text-center">
      <v-alert class="text-center text-blue-darken-2 text-h6">Downloading data to '{{exportUrl}}'</v-alert>
    </v-card-item>
  </v-card>
    </v-row>
</template>

<script setup lang="ts">
import apiClient from '@/plugins/axios';
import {ref} from "vue";

const exportUrl = ref('');

const exportData = async () => {
  const response = await apiClient.get('/export');
  const data = response.data;
  const date = new Date().toISOString().split('T')[0];

  const link = document.createElement('a');
  const blob = new Blob([JSON.stringify(data, null, 2)], {type: 'application/json'});
  link.href = URL.createObjectURL(blob);
  link.download = `${date}_flashcards_${data.username}.json`;
  document.body.appendChild(link);
  exportUrl.value = link.download;
  link.click();
  document.body.removeChild(link);
};
</script>

