import {mount} from "@vue/test-utils";
import {mountOptions} from "../util/useTestPlugins.ts";
import MainLayout from "@/layouts/MainLayout.vue";
import router from "@/router";

it('is mounted if route is /', async () => {
  const wrapper = mount(MainLayout, mountOptions);
  await router.push('/');
  expect(wrapper.text()).toContain('Welcome to');
  expect(wrapper.text()).toContain('Flashcards');
});
