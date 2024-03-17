<template>
  <base-card-page>
    <v-card-title v-text="'Export your data'" class="text-center text-h4"/>
    <v-card-subtitle v-text="'Export all your flashcards data to a Json file'" class="text-center"/>

    <v-card-item class="text-center">
      <v-alert class="text-deep-orange-darken-3">Note: For large data, export may take a moment.
        Find the Json file in browser downloads.
      </v-alert>
    </v-card-item>

    <v-card-item>
      <v-img src="@/assets/folder.jpg" aspect-ratio="2.25" class="mx-auto"/>
      <v-label class="float-right text-sm-subtitle-2 text-grey">Photo by&nbsp;<a
        href="https://unsplash.com/@qwitka?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Maksym Kaharlytskyi</a>&nbsp;on&nbsp;<a
        href="https://unsplash.com/photos/file-cabinet-Q9y3LRuuxmg?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
      </v-label>
    </v-card-item>

    <v-card-text class="mt-5 mb-5">
      <v-row justify="center">
        <v-form cols="12" sm="6" md="4">
          <input type="submit" hidden/><!-- Required for the form to submit on enter -->
          <v-btn color="black" border @click="exportData" variant="text">
            Start Export
          </v-btn>
        </v-form>
      </v-row>
    </v-card-text>

    <v-card-item v-if="filename" class="text-center">
      <v-alert class="text-center text-blue-darken-2 text-h6">Downloading data to '{{ filename }}'</v-alert>
    </v-card-item>
  </base-card-page>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useDataIoService from "@/feature/dataio/composables/useDataIoService.ts";
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";

const filename = ref('');

const exportData = async () => {
  const exportData = await useDataIoService().getUserExport();
  const blob = new Blob([JSON.stringify(exportData, null, 2)], {type: 'application/json'});
  const date = new Date().toISOString().split('T')[0];
  filename.value = `${date}_flashcards_${exportData.username}.json`;

  useDataIoService().makeDownload(filename.value, blob);
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: grey;
}
</style>
