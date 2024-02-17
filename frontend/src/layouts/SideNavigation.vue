<template>
  <v-navigation-drawer color="primary">
    <v-list-item v-html="`<strong>Flashcards</strong>`"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/categories" title="Your card categories"></v-list-item>
    <v-list-item to="/register" title="User registration"></v-list-item>
    <v-divider></v-divider>
    <v-list-item to="/" title="Welcome"></v-list-item>
  </v-navigation-drawer>
</template>
