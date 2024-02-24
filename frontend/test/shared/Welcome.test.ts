import {mount} from "@vue/test-utils";
import {mountOptions} from "../util/useTestPlugins.ts";
import MainLayout from "@/layouts/MainLayout.vue";
import router from "@/router";

it('is mounted if route is /', async () => {
  await router.push('/');
  const wrapper = mount(MainLayout, mountOptions);
  expect(wrapper.text()).toContain('Welcome to');
  expect(wrapper.text()).toContain('Flashcards');
});
