<template>
  <v-container>
    <v-responsive class="align-center text-center fill-height">
      <v-img height="300" src="@/assets/logo.svg"/>
      <div class="text-body-2 font-weight-light mb-n1">Welcome to</div>
      <h1 class="text-h2 font-weight-bold">Flashcards</h1>
      <div class="py-14"/>

      <v-row class="justify-center mb-2">
        <v-col cols="auto">
          <v-btn color="red" border @click="testAxiosCall('get')" variant="text">
            <v-icon icon="mdi-account-group" size="large" start/>
            test axios GET
          </v-btn>
          <v-btn color="red" border @click="testAxiosCall('post')" variant="text">
            <v-icon icon="mdi-account-group" size="large" start/>
            test axios POST
          </v-btn>
          <v-btn color="red" border @click="testAxiosCall('error', 'throw')" variant="text">
            <v-icon icon="mdi-account-group" size="large" start/>
            test throw ERROR
          </v-btn>
          <v-btn color="red" border @click="testAxiosCall('error', 'toast/console')" variant="text">
            <v-icon icon="mdi-account-group" size="large" start/>
            test custom notify ERROR
          </v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" sm="6" md="4">
          <v-btn color="black" border @click="useCardsService().getCategories" variant="text">
            Get categories
          </v-btn>
        </v-col>
        <v-col cols="12" sm="6" md="4">
          <v-text-field v-model="email" label="Email">
          </v-text-field>
          <v-text-field v-model="password" label="Password" type="password">
          </v-text-field>
          <v-btn color="black" border @click="postNewUser" variant="text">
            Register new user
          </v-btn>
        </v-col>
      </v-row>
    </v-responsive>
  </v-container>
</template>

<script setup lang="ts">
import {useAxiosTestService} from "@/shared/composables/testService";
import useCardsService from "@/feature/cards/composables/useCardsService";
import {ref} from "vue";

const email = ref('');
const password = ref('');

const testAxiosCall = (method: string, errorResult?: string) => {
  useAxiosTestService().testAxiosCall(method, errorResult);
}

const postNewUser = () => {
  useCardsService().postNewUser(email.value, password.value);
}
</script>

<style scoped lang="scss"/>
