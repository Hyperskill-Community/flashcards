<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between"
            fill-height
            color="containerBackground">
      <v-card-title class="text-center text-h4">
        User Registration
      </v-card-title>
      <v-container>
        <v-row>
          <v-col cols="12" sm="6" md="4"/>
          <v-col cols="12" sm="6" md="4" class="align-content-center">
            <v-text-field v-model="email" label="Email"/>
            <v-text-field v-model="password" label="Password" type="password"/>
            <v-btn color="black" border @click="postNewUser" variant="text">
              Register new user
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import useCardsService from "@/feature/cards/composables/useCardsService";
import {ref} from "vue";

const email = ref('');
const password = ref('');

const postNewUser = () => {
  useCardsService().postNewUser(email.value, password.value);
}
</script>

<style scoped lang="scss"/>
