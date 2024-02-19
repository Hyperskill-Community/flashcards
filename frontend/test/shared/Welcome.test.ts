import MainLayout from "@/layouts/MainLayout.vue";
import {mount} from "@vue/test-utils";
import router from "@/router";
import {mountOptions} from "../util/useTestPlugins.ts";

it('is mounted if route is /', async () => {
  await router.push('/');
  const wrapper = mount(MainLayout, mountOptions);
  expect(wrapper.text()).toContain('Welcome to');
  expect(wrapper.text()).toContain('Flashcards');
});
