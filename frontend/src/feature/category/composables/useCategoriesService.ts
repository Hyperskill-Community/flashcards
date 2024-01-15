import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {Category} from "@/feature/category/model/category";
import {inject} from "vue";

const apiBase = inject('apiBaseURL');
const useCategoriesService = () => {

  const getCategories = async (errorResult: string = 'throw') : Promise<Category[]> => {
    const url = apiBase + 'categories';

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

  return {
    getCategories,
  }
}
export default useCategoriesService;
