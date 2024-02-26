import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import {ActionType} from "@/feature/category/model/category.ts";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import CategoryOverviewPage from "@/feature/category/pages/CategoryOverviewPage.vue";
import flushPromises from "flush-promises";

vi.mock("@/feature/category/composables/useCategoriesService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      getCategories: vi.fn(),
      postNewCategory: vi.fn(),
      deleteCategory: vi.fn(),
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

describe('CategoryOverviewPage', () => {

  const categories = [
    {id: '1', name: 'Test Category', actions: [{action: ActionType.DELETE, uri: 'a'}], description: 'Test Description'},
    {id: '2', name: 'Other Category', actions: [], description: 'Other Description'}
  ];
  const expected = [
    {...categories[0]},
    {...categories[1]},
  ];
  const page = {
    categories: categories,
    currentPage: 0,
    totalElements: 2,
    isLast: true
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should fetch categories on mount and propagate them', async () => {
    vi.mocked(useCategoriesService().getCategories).mockResolvedValue(page);
    const wrapper = mount(CategoryOverviewPage, mountOptions);
    const iterator = wrapper.findComponent({name: 'CategoryIterator'});
    await vi.waitFor( // just as alternative to flushPromises - i.e. without that module
      () => {
        if (!iterator.vm.$props.categories.length)
          throw new Error('categories not loaded. Timeout.');
      }, {
        timeout: 100, // default is 1000
        interval: 10, // default is 50
      });
    expect(iterator.vm.$props.categories).toHaveLength(2);
    expect(useCategoriesService().getCategories).toHaveBeenCalled();
    // check if the categories and their counts are propagated to the CategoryIterator
    expect(iterator.vm.$props.categories[0]).toStrictEqual({category: expected[0], expanded: false});
    expect(iterator.vm.$props.categories[1]).toStrictEqual({category: expected[1], expanded: false});
  });

  it('should add category card to display after post', async () => {
    vi.mocked(useCategoriesService().getCategories).mockResolvedValueOnce(page);
    const wrapper = mount(CategoryOverviewPage, mountOptions);
    const iterator = wrapper.findComponent({name: 'CategoryIterator'});
    await flushPromises(); // slicker version of vi.waitFor
    expect(iterator.vm.$props.categories).toHaveLength(categories.length);
    const newCategories
      = [...categories, {id: '3', name: 'New Category', actions: [], description: 'New Description'}];
    vi.mocked(useCategoriesService().getCategories).mockResolvedValue({
      categories: newCategories,
      currentPage: 0,
      totalElements: 3,
      isLast: true
    });
    await wrapper.findComponent('.add-button').trigger('click');
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue(newCategories[2].name);
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue(newCategories[2].description);
    await wrapper.findComponent('.submit-button').trigger('click');
    await flushPromises();
    expect(iterator.vm.$props.categories).toHaveLength(newCategories.length);
  });

  it('should delete category card to display after delete', async () => {
    vi.mocked(useCategoriesService().getCategories).mockResolvedValueOnce(page);
    const wrapper = mount(CategoryOverviewPage, mountOptions);
    const iterator = wrapper.findComponent({name: 'CategoryIterator'});
    await flushPromises(); // slicker version of vi.waitFor
    expect(iterator.vm.$props.categories).toHaveLength(categories.length);
    vi.mocked(useCategoriesService().getCategories).mockResolvedValueOnce({
      categories: [categories[1]],
      currentPage: 0,
      totalElements: 1,
      isLast: true
    });
    await wrapper.findComponent('.delete-button').trigger('click');
    await flushPromises();
    expect(iterator.vm.$props.categories).toHaveLength(1);
    expect(iterator.vm.$props.categories).toEqual([{category: expected[1], expanded: false}]);
  });

});
