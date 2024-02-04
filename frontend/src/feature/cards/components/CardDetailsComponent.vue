<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <v-card color="secondary"
                class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between">
          <v-card-title>
            {{ card.title }}
          </v-card-title>
          <v-card-subtitle>
            <v-list class="ma-0 pa-0 d-flex flex-row flex-wrap">
              <v-list-item
                v-for="tag in card.tags"
                :title="tag as string"
              >
              </v-list-item>
            </v-list>
          </v-card-subtitle>
          <v-card-text>
            {{ card.question }}
            <v-container class="pa-0 mt-2">
              <v-container class="pa-0">
                Choices:
              </v-container>
              <v-list class="pa-0 d-flex flex-column flex-wrap">
                <v-list-item
                  v-for="(option, index) in card.options"
                  :title="(index + 1) + './' + option as string"
                >
                </v-list-item>
              </v-list>
            </v-container>
            <v-container class="pa-0 mt-2">
              Answer: {{ card.correctOption }}
            </v-container>

          </v-card-text>
          <v-container>
            <v-card-actions class="pa-0 ma-0">
              <edit-mdi-button tooltip-text="Edit Card - not implemented"/>
              <delete-mdi-button tooltip-text="Delete Card - not implemented"/>
              <v-spacer></v-spacer>
              <back-mdi-button tooltip-text="Back to Category"
                               :click-handler="$router.back"/>
            </v-card-actions>
          </v-container>
        </v-card>
      </v-col>
    </v-row>

  </v-container>
</template>

<script setup lang="ts">
import EditMdiButton from "@/shared/components/EditMdiButton.vue";
import DeleteMdiButton from "@/shared/components/DeleteMdiButton.vue";
import {Card} from "@/feature/cards/model/card";
import BackMdiButton from "@/shared/components/BackMdiButton.vue";

defineProps<({
  card: Card,
})>()
</script>
