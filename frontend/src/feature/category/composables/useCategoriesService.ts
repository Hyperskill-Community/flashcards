import {Category, CategoryPage, CategoryRequest} from "@/feature/category/model/category";
import useApi from "@/shared/composables/useApi";

const useCategoriesService = () => {

  const ENDPOINT = '/categories';

  const getCategories = async (page: number = 0) => {
    return await useApi().get<CategoryPage>(ENDPOINT, {page: String(page)});
  };

  const getCategoryById = async (id: string) => {
    return await useApi().get<Category>(`${ENDPOINT}/${id}`);
  };

  const postNewCategory = async (data: CategoryRequest) => {
    return await useApi().post(ENDPOINT, data, `Category ${data.name} successfully created!`);
  };

  const putCategory = async (id: string, data: CategoryRequest): Promise<Category> => {
    return await useApi().put(`${ENDPOINT}/${id}`, data, {},
      `Category ${data.name} successfully updated!`);
  };

  const deleteCategory = async (id: string) => {
    return await useApi().delete(`${ENDPOINT}/${id}`);
  };

  return {
    getCategories,
    postNewCategory,
    putCategory,
    deleteCategory,
    getCategoryById
  };
};

export default useCategoriesService;
