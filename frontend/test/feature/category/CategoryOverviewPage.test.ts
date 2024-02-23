import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import useCardsService from "@/feature/cards/composables/useCardsService.ts";
import CategoryOverviewPage from "@/feature/category/pages/CategoryOverviewPage.vue";
import flushPromises from 'flush-promises';

vi.mock("@/feature/category/composables/useCategoriesService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      getCategories: vi.fn(),
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
    {id: '1', name: 'Test Category', actions: [], description: 'Test Description'},
    {id: '2', name: 'Other Category', actions: [], description: 'Other Description'}
  ];

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should fetch categories and their counts on mount and propagate them', async () => {
    vi.mocked(useCategoriesService().getCategories).mockResolvedValue(categories);
    vi.mocked(useCardsService().getCardCount).mockResolvedValue(2);
    const wrapper = mount(CategoryOverviewPage, mountOptions);
    await flushPromises(); // wait for all async calls to finish
    expect(useCategoriesService().getCategories).toHaveBeenCalled();
    expect(useCardsService().getCardCount).toHaveBeenCalledWith('1');
    expect(useCardsService().getCardCount).toHaveBeenCalledWith('2');
    // check if the categories and their counts are propagated to the CategoryIterator
    const iterator = wrapper.findComponent({name: 'CategoryIterator'});
    expect(iterator.vm.$props.categories).toHaveLength(2);
    expect(iterator.vm.$props.categories[0]).toEqual({category: categories[0], expanded: false});
    expect(iterator.vm.$props.categories[1]).toEqual({category: categories[1], expanded: false});
  });

});
