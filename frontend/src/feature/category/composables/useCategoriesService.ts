import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {Category, CategoryRequest} from "@/feature/category/model/category";
import apiUrl from "@/shared/composables/baseUrl";
import {useToastService} from "@/shared/composables/toastService";

const useCategoriesService = () => {
  const getCategories = async (errorResult: string = 'throw') : Promise<Category[]> => {
    const url = `${apiUrl}categories`;
    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        useErrorService().handleAndNotify(`Error status code ${response.status}!`, 'Failed to load categories');
        return [];
      } else {
        return (response.data.categories as Category[]);
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return [];
    }
  }

  const getCategoryById = async (id: String, errorResult: string = 'throw') : Promise<Category> => {
    const url = `${apiUrl}categories/${id}`;
    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        useErrorService().handleAndNotify(`Error status code ${response.status}!`, 'Failed to load category');
        return {} as Category;
      } else {
        console.log(response.data);
        return (response.data as Category);
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return {} as Category;
    }
  }


  const postNewCategory = async (data: CategoryRequest, errorResult: string = 'throw')  => {
    const url = `${apiUrl}categories`;

    try {
      const response = await apiClient.post(url, data);
      if (response.status !== 201) {
        useErrorService().handleAndNotify(`Error status code ${response.status}!`, 'Failed to post card');
      } else {
        useToastService().showSuccess('SUCCESS', `Category ${data.name} successfully created!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  const putCategory = async (id: string, data: CategoryRequest, errorResult: string = 'throw')  => {
    const url = `${apiUrl}categories/${id}`;

    try {
      const response = await apiClient.put(url, data);
      if (response.status !== 200) {
        useErrorService().handleAndNotify(`Error status code ${response.status}!`, 'Failed to update card');
      } else {
        useToastService().showSuccess('SUCCESS', `Category ${data.name} successfully updated!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  const deleteCategory = async (id: string, errorResult: string = 'throw')  => {
    const url = `${apiUrl}categories/${id}`;

    try {
      const response = await apiClient.delete(url);
      if (response.status !== 200) {
        useErrorService().handleAndNotify(`Error status code ${response.status}!`, 'Failed to delete card');
      } else {
        useToastService().showSuccess('SUCCESS', `Category successfully removed`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  return {
    getCategories,
    postNewCategory,
    putCategory,
    deleteCategory,
    getCategoryById
  }
}
export default useCategoriesService;
