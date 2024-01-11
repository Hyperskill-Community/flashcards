import apiClient from '@/plugins/axios';
import {useErrorService} from "@/shared/composables/errorService";
import {useToastService} from "@/shared/composables/toastService";
import {Category} from "@/feature/cardmanagement/model/category";

const useCardsService = () => {

  const postNewUser = async (email: string, password: string, errorResult: string = 'throw') => {
    const postUrl = 'http://127.0.0.1:8080/api/register';

    const postData = {
      email: email,
      password: password,
    };

    try {
      const response = await apiClient.post(postUrl, postData);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        useToastService().showSuccess('SUCCESS', `User ${email} registered successfully!`)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  const getCategories = async (errorResult: string = 'throw') => {
    const url = 'http://127.0.0.1:8080/api/categories';

    try {
      const response = await apiClient.get(url);
      if (response.status !== 200) {
        throw new Error(`Error status code ${response.status}!`);
      } else {
        const categories = (response.data.categories as Category[]);
        var result = 'Found categories:\n'
        categories.forEach((category: Category) => {
          result += `Id: ${category.id}, Name: ${category.name}, Access ${category.access}\n`;
        });
        useToastService().showSuccess('SUCCESS', result)
      }
    } catch (error: any) {
      errorResult === 'throw' ? useErrorService().handleAndThrow(error)
        : useErrorService().handleAndNotify('custom error', 'custom message');
    }
  }

  return {
    postNewUser,
    getCategories,
  }
}
export default useCardsService;
