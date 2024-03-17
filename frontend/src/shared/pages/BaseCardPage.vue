<template>
  <v-container>
    <v-row justify="center">
      <v-col sm="12" md="10" lg="8">
        <v-card color="secondary" class="pa-2 ma-2 d-flex flex-column">
          <slot></slot>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
