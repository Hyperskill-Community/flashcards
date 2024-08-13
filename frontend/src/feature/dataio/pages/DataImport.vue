<template>
  <base-card-page>
    <v-card-title
      class="text-center text-h4"
      v-text="'Import data'"
    />
    <v-card-subtitle
      class="text-center"
      v-text="'Import a JSON file with your flashcards data'"
    />
    <v-card-item class="text-center">
      <v-alert class="text-deep-orange-darken-3">
        Note: For large data sets, the import process may take a moment.
      </v-alert>
    </v-card-item>
    <v-card-item>
      <v-img
        src="@/assets/import.jpg"
        aspect-ratio="2.25"
        class="mx-auto"
        gradient="linear"
      />
      <v-label class="float-right text-sm-subtitle-2 text-grey">
        Photo by&nbsp;<a
          href="https://unsplash.com/@chuttersnap?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash"
        >CHUTTERSNAP</a>&nbsp;on&nbsp;<a
          href="https://unsplash.com/photos/aerial-photo-of-cargo-crates-fN603qcEA7g?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash"
        >Unsplash</a>
      </v-label>
    </v-card-item>

    <v-card-text>
      <v-row justify="center">
        <v-col>
          <v-form
            class="d-flex align-center justify-lg-center"
            @submit.prevent
          >
            <v-file-input
              v-model="filePath"
              density="compact"
              label="Choose .json file to import"
              accept="application/json"
              prepend-icon="mdi-camera"
              class="v-col-sm-10"
            />
            <submit-mdi-button
              :disabled="!filePath.length"
              :click-handler="importData"
              tooltip-text="Import Json data"
            />
          </v-form>
        </v-col>
      </v-row>
    </v-card-text>
  </base-card-page>
</template>

<script setup lang="ts">
import {ref} from "vue";
import SubmitMdiButton from "@/shared/buttons/SubmitMdiButton.vue";
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";

const filePath = ref<File[]>([]);

const importData = () => {
  console.log(filePath.value[0].text());
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: grey;
}
</style>
