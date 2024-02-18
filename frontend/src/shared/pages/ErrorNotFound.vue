<template>
  <div class="fullscreen text-center q-pa-md flex flex-center">
    <div>
      <div v-text="'404'" style="font-size: 30vh"/>
      <div v-text="'Oops. Nothing here...'" class="text-h2" style="opacity: 0.5"/>
      <v-btn v-text="'Go Home'" class="ma-10" to="/"/>
    </div>
  </div>
</template>

<script setup lang="ts"/>
