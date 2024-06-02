import {createVuetify} from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import router from "@/router";
import mediaQuery from 'css-mediaquery';

const vuetify = createVuetify({
  components,
  directives,
});
global.ResizeObserver = require('resize-observer-polyfill');

export const mountOptions = {
  global: {plugins: [vuetify, router],}
};

// the follwing code is for testing the responsive design
// after: https://www.anthonygonzales.dev/blog/how-to-solve-match-media-is-not-a-function.html

beforeAll(() => {
  // Set the initial/default matchMedia implementation
  // for Mobile First development
  window.matchMedia = createMatchMedia('576px');
});

afterEach(() => {
  // Reset matchMedia after each test
  window.matchMedia = createMatchMedia('576px');
});

export const createMatchMedia = (width: any) =>
  (query: string) => ({
    matches: mediaQuery.match(query, {
      width,
    }),
    addListener: () => {},
    removeListener: () => {},
    addEventListener: () => {},
    removeEventListener: () => {},
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    dispatchEvent: (e: Event) => true,
    onchange: null,
    media: query,
  });
