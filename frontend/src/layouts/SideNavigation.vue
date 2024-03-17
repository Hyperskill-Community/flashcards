<template>
  <v-navigation-drawer color="primary">
    <v-list-item><strong>Flashcards</strong></v-list-item>
    <v-divider/>
    <v-list-item to="/categories" title="Your card categories"/>
    <v-list-item to="/export" title="Export your data"/>
    <v-list-item to="/import" title="Import data"/>
    <v-list-item to="/register" title="User registration"/>
    <v-divider/>
    <v-list-item to="/" title="Welcome"/>
  </v-navigation-drawer>
</template>
