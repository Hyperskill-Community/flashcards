import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import {ActionType} from "@/feature/category/model/category.ts";
import App from "@/App.vue";
import CategoryOverviewPage from "@/feature/category/pages/CategoryOverviewPage.vue";
import CategoryCard from "@/feature/category/components/CategoryCard.vue";
import router from "@/router";
import flushPromises from "flush-promises";

vi.mock("@/feature/category/composables/useCategoriesService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      deleteCategory: vi.fn(),
      putCategory: vi.fn(),
      getCategories: vi.fn(),
      getCategoryById: vi.fn(),
    })
  };
});

vi.mock("@/feature/cards/composables/useCardsService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      getCardCount: vi.fn(),
    })
  };
});

describe('CategoryCard', () => {

  const category = {
    id: '1',
    name: 'Test Category',
    actions: [{action: ActionType.WRITE, uri: '/write'}],
    description: 'Test Description',
    numberOfCards: 5
  };

  const page = {
    categories: [category],
    currentPage: 0,
    totalElements: 1,
    isLast: true
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should open and close edit form on edit button click', async () => {
    const wrapper = mount(CategoryCard, {
      ...mountOptions,
      props: {category: category, expanded: false}
    });
    expect(wrapper.findComponent('.v-form').exists()).toBe(false);
    await wrapper.findComponent('.edit-button').trigger('click');
    expect(wrapper.findComponent('.v-form').isVisible()).toBe(true);
    await wrapper.findComponent('.edit-button').trigger('click');
    expect(wrapper.findComponent('.v-form').exists()).toBe(false);
  });

  it('should put filled form correctly and fire reload', async () => {
    const wrapper = mount(CategoryCard, {
      ...mountOptions,
      props: {category: category, expanded: false}
    });
    const putRequest = {name: 'New Name', description: 'New Description'};
    await wrapper.findComponent('.edit-button').trigger('click');
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue('New Name');
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue('New Description');
    await wrapper.findComponent('.submit-button').trigger('click');
    expect(useCategoriesService().putCategory).toHaveBeenCalledWith(category.id, putRequest);
    expect(wrapper.emitted('reload')).toBeTruthy();
  });

  it('should not delete category without delete right', async () => {
    const wrapper = mount(CategoryCard, {
      ...mountOptions,
      props: {category: category, expanded: false}
    });
    expect(wrapper.findComponent({name: 'DeleteMdiButton'}).vm.$props.disabled).toBeTruthy();
    await wrapper.findComponent('.delete-button').trigger('click');
    expect(useCategoriesService().deleteCategory).not.toHaveBeenCalled();
  });

  it('should route to card overview page on open button click', async () => {
    const app = mount(App, mountOptions);
    vi.mocked(useCategoriesService().getCategories).mockResolvedValue(page);
    vi.mocked(useCategoriesService().getCategoryById).mockResolvedValue(category);
    await router.push('/categories');
    vi.spyOn(router, 'push');
    await flushPromises();
    await app.findComponent('.open-button').trigger('click');
    expect(router.push).toHaveBeenCalledWith(`/category/${category.id}?name=${category.name}`);
    await router.push(`/category/${category.id}?name=${category.name}`); // since we mocked the service, we need to manually navigate
    expect(app.findComponent({name: 'CardsPage'}).exists()).toBeTruthy();
    await flushPromises(); // wait for getCategoryById to resolve
    expect(app.text()).toContain(category.name); // now the name should be visible
  });

  it('should show details on expand and close on switch', async () => {
    vi.mocked(useCategoriesService().getCategories).mockResolvedValue(page);
    const wrapper = mount(CategoryOverviewPage, {
      ...mountOptions,
    });
    await flushPromises();
    await wrapper.findComponent('.v-switch').trigger('click'); //expand
    expect(wrapper.findComponent({name: 'CategoryCard'}).emitted('update:expanded')).toBeTruthy();
    expect(wrapper.text()).toContain(' Your access: WRITE');
    await wrapper.findComponent('.v-switch').trigger('click'); // close
    expect(wrapper.text()).not.toContain(' Your access: WRITE');
  });

});
