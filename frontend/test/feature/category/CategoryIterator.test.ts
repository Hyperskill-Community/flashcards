import {mount} from "@vue/test-utils";
import {mountOptions} from "../../util/useTestPlugins.ts";
import {ActionType} from "@/feature/category/model/category.ts";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import CategoryIterator from "@/feature/category/components/CategoryIterator.vue";

vi.mock("@/feature/category/composables/useCategoriesService.ts", () => {
  return {
    default: vi.fn().mockReturnValue({
      postNewCategory: vi.fn(),
    })
  };
});

describe('CategoryIterator', () => {

  const categories = [
    {
      category: {
        id: '1',
        name: 'Test Category',
        actions: [{action: ActionType.WRITE, uri: '/write'}],
        description: 'Test Description',
        numberOfCards: 5
      }, expanded: false
    },
    {
      category: {id: '2', name: 'Other Category', actions: [], description: 'Other Description', numberOfCards: 10},
      expanded: false
    },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should contain as many CategoryCards as entries in prop', async () => {
    const wrapper = mount(CategoryIterator, {
      ...mountOptions,
      props: {categories: categories, itemsPerPage: 5, total: 2}
    });
    expect(wrapper.findAllComponents({name: 'CategoryCard'})).toHaveLength(2);
  });

  it('should open and close add form on add button click', async () => {
    const wrapper = mount(CategoryIterator, {
      ...mountOptions,
      props: {categories: categories, itemsPerPage: 5, total: 2}
    });
    expect(wrapper.findComponent('.v-form').exists()).toBe(false);
    await wrapper.findComponent('.add-button').trigger('click');
    expect(wrapper.findComponent('.v-form').isVisible()).toBe(true);
    await wrapper.findComponent('.add-button').trigger('click');
    expect(wrapper.findComponent('.v-form').exists()).toBe(false);
  });

  it('should post new category on form submit', async () => {
    const wrapper = mount(CategoryIterator, {
      ...mountOptions,
      props: {categories: categories, itemsPerPage: 5, total: 2}
    });
    const newCategory = {
      name: 'New Category',
      description: 'New Description',
    };
    await wrapper.findComponent('.add-button').trigger('click');
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue(newCategory.name);
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue(newCategory.description);
    expect(wrapper.findComponent({name: 'SubmitMdiButton'}).vm.$props.disabled).toBeFalsy();
    await wrapper.findComponent('.submit-button').trigger('click');
    expect(useCategoriesService().postNewCategory).toHaveBeenCalledWith(newCategory);
  });
2;
  it('should not post new category on form submit if name is empty', async () => {
    const wrapper = mount(CategoryIterator, {
      ...mountOptions,
      props: {categories: categories, itemsPerPage: 5, total: 2}
    });
    await wrapper.findComponent('.add-button').trigger('click');
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue('New Description');
    expect(wrapper.findComponent({name: 'SubmitMdiButton'}).vm.$props.disabled).toBeTruthy();
    await wrapper.findComponent('.submit-button').trigger('click');
    expect(useCategoriesService().postNewCategory).not.toHaveBeenCalled();
  });

  it('should fire reload event after post', async () => {
    const wrapper = mount(CategoryIterator, {
      ...mountOptions,
      props: {categories: categories, itemsPerPage: 5, total: 2}
    });
    await wrapper.findComponent('.add-button').trigger('click');
    await wrapper.findAllComponents('.v-text-field')?.at(0)?.setValue('New Category');
    await wrapper.findAllComponents('.v-text-field')?.at(1)?.setValue('New Description');
    await wrapper.findComponent('.submit-button').trigger('click');
    expect(wrapper.emitted('reload')).toBeTruthy();
  });

});
