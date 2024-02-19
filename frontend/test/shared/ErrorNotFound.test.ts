import {mount} from '@vue/test-utils';
import ErrorNotFound from '@/shared/pages/ErrorNotFound.vue';
import {describe, expect, it} from "vitest";
import {createVuetify} from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import router from "@/router";

const vuetify = createVuetify({
  components,
  directives,
});
const mountOptions = {
  global: {plugins: [vuetify, router],}
};

describe('ErrorNotFound.vue', () => {
  it('renders 404 message', () => {
    const wrapper = mount(ErrorNotFound, mountOptions);
    expect(wrapper.text()).toContain('404');
  });

  it('renders "Oops. Nothing here..." message', () => {
    const wrapper = mount(ErrorNotFound, mountOptions);
    expect(wrapper.text()).toContain('Oops. Nothing here...');
  });

  it('renders "Go Home" button', () => {
    const wrapper = mount(ErrorNotFound, mountOptions);
    expect(wrapper.text()).toContain('Go Home');
  });

  it('redirects to home page when "Go Home" button is clicked', async () => {
    const wrapper = mount(ErrorNotFound, mountOptions);
    await wrapper.find('.ma-10').trigger('click');
    expect(wrapper.vm.$route.path).toBe('/');
  });
});
