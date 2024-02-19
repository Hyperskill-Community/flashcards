import {createVuetify} from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import router from "@/router";

const vuetify = createVuetify({
  components,
  directives,
});
global.ResizeObserver = require('resize-observer-polyfill');
export const mountOptions = {
  global: {plugins: [vuetify, router],}
};

