<template>
  <v-container>
    <v-card class="pa-2 ma-2 mx-auto d-flex flex-column justify-space-between" fill-height color="containerBackground">
      <v-card-title v-text="'User Registration'" class="text-center text-h4"/>
      <v-container>
        <v-row>
          <v-col cols="12" sm="6" md="4"/>
          <v-col cols="12" sm="6" md="4" class="align-content-center">
            <v-text-field v-model="email" label="Email"/>
            <v-text-field v-model="password" label="Password" type="password"/>
            <v-btn v-text="'Register new user'" color="black" border @click="postNewUser" variant="text"/>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useRegistrationService from "@/feature/registration/composables/useRegistrationService";

const email = ref('');
const password = ref('');

const postNewUser = () => useRegistrationService().postNewUser(email.value, password.value);
</script>
