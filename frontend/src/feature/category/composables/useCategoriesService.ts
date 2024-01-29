import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {Category} from "@/feature/category/model/category";
import apiUrl from "@/shared/composables/baseUrl";
import {useToastService} from "@/shared/composables/toastService";

const useCategoriesService = () => {
  const getCategories = async (errorResult: string = 'throw') : Promise<Category[]> => {
    const url = `${apiUrl}categories`;
    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        return (response.data.categories as Category[]);
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
      return [];
    }
  }

  const postNewCategory = async (name: string, errorResult: string = 'throw')  => {
    const url = `${apiUrl}categories`;
    const postData = {
      name: name
    };

    try {
      const response = await apiClient.post(url, postData);
      if (response.status !== 201) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        useToastService().showSuccess('SUCCESS', `Category ${name} successfully created!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
    return "posted";
  }

  const putCategory = async (id: string, newName: string, errorResult: string = 'throw')  => {
    const url = `${apiUrl}categories/${id}`;
    const putData = {
      name: newName
    };

    try {
      const response = await apiClient.put(url, putData);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        useToastService().showSuccess('SUCCESS', `Category successfully renamed to ${newName}!`)
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
        throw new Error(`Error status code ${response.status}!`);
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
    deleteCategory
  }
}
export default useCategoriesService;
