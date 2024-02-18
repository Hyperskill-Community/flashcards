<template>
  <v-navigation-drawer color="primary">
    <v-list-item v-html="`<strong>Flashcards</strong>`"/>
    <v-divider/>
    <v-list-item to="/categories" title="Your card categories"/>
    <v-list-item to="/register" title="User registration"/>
    <v-divider/>
    <v-list-item to="/" title="Welcome"/>
  </v-navigation-drawer>
</template>
