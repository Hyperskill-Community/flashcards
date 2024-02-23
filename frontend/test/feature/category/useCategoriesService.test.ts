import useApi from "@/shared/composables/useApi";
import useCategoriesService from "@/feature/category/composables/useCategoriesService.ts";
import {ActionType, Category, CategoryPage} from "@/feature/category/model/category.ts";

vi.mock('@/shared/composables/useApi', () => {
  return {
    default: vi.fn().mockReturnValue({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    })};
});

describe('useCategoriesService', () => {

  const readCat: Category = {
    id: '1',
    name: 'Test Category',
    description: 'Test Description',
    actions: [{action: ActionType.READ, uri: '/read' }]
  };

  const writeCat: Category = {
    id: '2',
    name: 'Other Category',
    description: 'Other Description',
    actions: [{action: ActionType.WRITE, uri: '/write' }]
  };

  const page : CategoryPage = {
    categories: [readCat, writeCat],
    currentPage: 1,
    isLast: true
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('getCategories should return categories in page', async () => {
    vi.mocked(useApi().get).mockResolvedValue(page);
    await expect(useCategoriesService().getCategories()).resolves.toEqual(page.categories);
    expect(useApi().get).toHaveBeenCalledWith('/categories');
  });

  it('getCategoryById should return category by id', async () => {
    vi.mocked(useApi().get).mockResolvedValue(readCat);
    await expect(useCategoriesService().getCategoryById('1')).resolves.toEqual(readCat);
    expect(useApi().get).toHaveBeenCalledWith('/categories/1');
  });

  it('postNewCategory should return category created', async () => {
    const newCat = {name: 'New Category', description: 'New Description'};
    await useCategoriesService().postNewCategory(newCat);
    expect(useApi().post).toHaveBeenCalledWith('/categories', newCat, 'Category New Category successfully created!');
  });


  it('putCategory should return category updated', async () => {
    const updatedCat = {name: 'Updated Category', description: 'Updated Description'};
    await useCategoriesService().putCategory('1', updatedCat);
    expect(useApi().put).toHaveBeenCalledWith('/categories/1', updatedCat, 'Category Updated Category successfully updated!');
  });

  it('deleteCategory should return category removed', async () => {
    await useCategoriesService().deleteCategory('1');
    expect(useApi().delete).toHaveBeenCalledWith('/categories/1', 'Category successfully removed');
  });

});
