<template>
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
</template>

<script setup lang="ts">
import {useAxiosTestService} from "@/shared/composables/testService";

const testAxiosCall = (method: string, errorResult?: string) => {
  useAxiosTestService().testAxiosCall(method, errorResult);
}
</script>

<style scoped lang="scss"/>
