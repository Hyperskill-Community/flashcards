<template>
  <div class="fullscreen text-center q-pa-md flex flex-center">
    <div>
      <div
        style="font-size: 30vh"
        v-text="'404'"
      />
      <div
        class="text-h2"
        style="opacity: 0.5"
        v-text="'Oops. Nothing here...'"
      />
      <v-btn
        class="ma-10"
        to="/"
        v-text="'Go Home'"
      />
    </div>
  </div>
</template>

