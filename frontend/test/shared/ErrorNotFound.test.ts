import {mount} from '@vue/test-utils';
import {mountOptions} from "../util/useTestPlugins.ts";
import ErrorNotFound from '@/shared/pages/ErrorNotFound.vue';
import MainLayout from "@/layouts/MainLayout.vue";
import router from "@/router";

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
    await wrapper.findComponent('.v-btn').trigger('click');
    expect(wrapper.vm.$route.path).toBe('/');
  });

  it('is mounted if route is not found', async () => {
    await router.push('/not-there');
    const wrapper = mount(MainLayout, mountOptions);
    expect(wrapper.text()).toContain('404');
  });
});
