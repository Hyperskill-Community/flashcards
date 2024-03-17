<template>
  <base-card-page>
    <v-card-title v-text="'User Registration'" class="text-center text-h4"/>
    <v-card-item>
      <v-img src="@/assets/registration.jpg" aspect-ratio="2.25" class="mx-auto" gradient="linear"/>
      <v-label class="float-right text-sm-subtitle-2 text-grey">Photo by&nbsp;<a
        href="https://unsplash.com/@daiangan?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Daian Gan</a>&nbsp;on&nbsp;<a
        href="https://unsplash.com/photos/brown-paper-on-white-table-8_d05sj9JVc?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
      </v-label>
    </v-card-item>
    <v-card-text>
      <v-form @submit.prevent="() => !!formValid" v-model="formValid">
        <v-row class="d-flex flex-column align-center pa-0">
          <input type="submit" hidden/><!-- Required for the form to submit on enter -->
          <v-text-field density="compact" v-model="email" label="Email" class="v-col-sm-8 mt-2 mb-n6"
                        :rules="[v => /.+@.+\..+/.test(v) || 'enter valid email']"/>
          <v-text-field density="compact" v-model="password" label="Password" type="password" class="v-col-sm-8"
                        :rules="[v => v.length>=8  || 'Password min. 8 chars']"/>
          <v-btn v-text="'Register new user'" color="black" @click="postNewUser" :disabled="!formValid"
                 variant="outlined"/>
        </v-row>
      </v-form>
    </v-card-text>
  </base-card-page>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useRegistrationService from "@/feature/registration/composables/useRegistrationService";
import BaseCardPage from "@/shared/pages/BaseCardPage.vue";

const email = ref('');
const password = ref('');
const formValid = ref(false);

const postNewUser = () => useRegistrationService().postNewUser(email.value, password.value);
</script>

<style scoped>
a {
  text-decoration: none;
  color: grey;
}
</style>
