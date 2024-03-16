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

      <v-card-text class="mt-10 mb-5">
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
        <v-alert class="text-center text-blue-darken-2 text-h6">Downloading data to '{{ exportUrl }}'</v-alert>
      </v-card-item>
    </v-card>
  </v-row>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useDataIoService from "@/feature/dataio/composables/useDataIoService.ts";

const exportUrl = ref('');

const exportData = async () => {
  const exportData = await useDataIoService().getUserExport();
  const blob = new Blob([JSON.stringify(exportData, null, 2)], {type: 'application/json'});
  const date = new Date().toISOString().split('T')[0];
  exportUrl.value = `${date}_flashcards_${exportData.username}.json`;

  useDataIoService().makeDownload(exportUrl.value, blob);
};
</script>

